package com.ren.admin.controller.localstorage;

import com.ren.common.domain.model.dto.AjaxResultDTO;
import com.ren.common.properties.LocalStorageProperties;
import com.ren.common.utils.ServletUtils;
import com.ren.common.utils.StringUtils;
import com.ren.common.utils.file.FileUtils;
import com.ren.localstorage.service.LocalStorageService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private LocalStorageService localStorageService;

    /**
     * 上传文件至本地（单个）
     * @param file
     * @return com.ren.localstorage.domain.dto.AjaxResultDTO
     * @author ren
     * @date 2025/06/21 15:26
     */
    @PostMapping("/upload")
    public AjaxResultDTO uploadFile(MultipartFile file, String belong) throws Exception
    {
        try
        {
            // 上传文件路径
            String filePath = LocalStorageProperties.getUploadPath(belong);
            // 上传并返回新文件名称
            String lastFilePath = localStorageService.uploadV2(filePath, file);
            String url = ServletUtils.getUrl() + lastFilePath;
            AjaxResultDTO ajax = AjaxResultDTO.success();
            ajax.put("url", url);
            ajax.put("fileName", lastFilePath);
            ajax.put("newFileName", FileUtils.getName(lastFilePath));
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
    public AjaxResultDTO uploadFiles(List<MultipartFile> files, String belong) throws Exception
    {
        try
        {
            // 上传文件路径
            String filePath = LocalStorageProperties.getUploadPath(belong);
            List<String> urls = new ArrayList<String>();
            List<String> fileNames = new ArrayList<String>();
            List<String> newFileNames = new ArrayList<String>();
            List<String> originalFilenames = new ArrayList<String>();
            for (MultipartFile file : files)
            {
                // 上传并返回新文件名称
                String fileName = localStorageService.uploadV2(filePath, file);
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
     * 下载指定路径下（除基础路径外的路径，路径最开头是文件请求前缀/profile，文件结尾是文件名）的文件
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