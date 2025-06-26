package com.ren.admin.controller.localstorage;

import com.ren.common.domain.model.dto.AjaxResultDTO;
import com.ren.common.properties.LocalStorageProperties;
import com.ren.common.utils.DateUtils;
import com.ren.common.utils.ServletUtils;
import com.ren.common.utils.StringUtils;
import com.ren.common.utils.file.FileUploadUtils;
import com.ren.common.utils.file.FileUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/localStorage")
@Slf4j
public class LocalStorageController {

    private static final String FILE_DELIMETER = ",";

    /**
     * 上传文件至本地（单个）
     * @param file
     * @return com.ren.localstorage.domain.dto.AjaxResultDTO
     * @author ren
     * @date 2025/06/21 15:26
     */
    @PostMapping("/upload")
    public AjaxResultDTO uploadFile(MultipartFile file) throws Exception
    {
        try
        {
            // 上传文件路径
            String filePath = LocalStorageProperties.getUploadPath();
            // 上传并返回新文件名称
            String fileName = FileUploadUtils.upload(filePath, file);
            String url = ServletUtils.getUrl() + fileName;
            AjaxResultDTO ajax = AjaxResultDTO.success();
            ajax.put("url", url);
            ajax.put("fileName", fileName);
            ajax.put("newFileName", FileUtils.getName(fileName));
            ajax.put("originalFilename", file.getOriginalFilename());
            return ajax;
        }
        catch (Exception e)
        {
            return AjaxResultDTO.error(e.getMessage());
        }
    }

    /**
     * 上传文件至本地（多个）
     * @param files
     * @return com.ren.localstorage.domain.dto.AjaxResultDTO
     * @author ren
     * @date 2025/06/21 15:25
     */
    @PostMapping("/uploads")
    public AjaxResultDTO uploadFiles(List<MultipartFile> files) throws Exception
    {
        try
        {
            // 上传文件路径
            String filePath = LocalStorageProperties.getUploadPath();
            List<String> urls = new ArrayList<String>();
            List<String> fileNames = new ArrayList<String>();
            List<String> newFileNames = new ArrayList<String>();
            List<String> originalFilenames = new ArrayList<String>();
            for (MultipartFile file : files)
            {
                // 上传并返回新文件名称
                String fileName = FileUploadUtils.upload(filePath, file);
                String url = ServletUtils.getUrl() + fileName;
                urls.add(url);
                fileNames.add(fileName);
                newFileNames.add(FileUtils.getName(fileName));
                originalFilenames.add(file.getOriginalFilename());
            }
            AjaxResultDTO ajax = AjaxResultDTO.success();
            ajax.put("urls", StringUtils.join(FILE_DELIMETER, urls));
            ajax.put("fileNames", StringUtils.join(FILE_DELIMETER, fileNames));
            ajax.put("newFileNames", StringUtils.join(FILE_DELIMETER, newFileNames));
            ajax.put("originalFilenames", StringUtils.join(FILE_DELIMETER, originalFilenames));
            return ajax;
        }
        catch (Exception e)
        {
            return AjaxResultDTO.error(e.getMessage());
        }
    }

    /**
     * 根据名称去下载默认下载路径下的文件
     * @param fileName 文件名称
     * @param delete 是否删除
     * @author ren
     * @date 2025/06/21 15:23
     */
    @GetMapping("/download")
    public void fileDownload(String fileName, Boolean delete, HttpServletResponse response, HttpServletRequest request)
    {
        try
        {
            if (!FileUtils.checkAllowDownload(fileName))
            {
                throw new Exception(StringUtils.format("文件名称({})非法，不允许下载。 ", fileName));
            }
            // 文件下载路径
            String filePath = LocalStorageProperties.getDownloadPath() + fileName;
            // 下载的新文件名
            String downloadName = DateUtils.current() + fileName.substring(fileName.indexOf("_") + 1);

            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            FileUtils.setAttachmentResponseHeader(response, downloadName);
            FileUtils.writeBytes(filePath, response.getOutputStream());
            if (delete)
            {
                FileUtils.deleteFile(filePath);
            }
        }
        catch (Exception e)
        {
            log.error("下载文件失败", e);
        }
    }

    /**
     * 下载指定路径下的文件
     * @param resource
     * @param request
     * @param response
     * @author ren
     * @date 2025/06/21 15:24
     */
    @GetMapping("/download/resource")
    public void resourceDownload(String resource, HttpServletRequest request, HttpServletResponse response)
            throws Exception
    {
        try
        {
            if (!FileUtils.checkAllowDownload(resource))
            {
                throw new Exception(StringUtils.format("资源文件({})非法，不允许下载。 ", resource));
            }
            // 文件下载路径
            String downloadPath = LocalStorageProperties.getProfile() + FileUtils.stripPrefix(resource);
            // 下载的新文件名
            String downloadName = StringUtils.subAfter(downloadPath, "/",true);

            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            FileUtils.setAttachmentResponseHeader(response, downloadName);
            FileUtils.writeBytes(downloadPath, response.getOutputStream());
        }
        catch (Exception e)
        {
            log.error("下载文件失败", e);
        }
    }

}