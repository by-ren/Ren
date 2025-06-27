package com.ren.localstorage.service.impl;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Objects;

import com.ren.common.utils.StringUtils;
import com.ren.localstorage.entity.ImageLog;
import com.ren.localstorage.mapper.ImageLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ren.common.domain.constant.MimeTypeConstants;
import com.ren.common.domain.exception.file.FileNameLengthLimitExceededException;
import com.ren.common.domain.exception.file.FileSizeLimitExceededException;
import com.ren.common.domain.exception.file.InvalidExtensionException;
import com.ren.common.utils.DateUtils;
import com.ren.common.utils.ServletUtils;
import com.ren.common.utils.file.FileUploadUtils;
import com.ren.localstorage.service.LocalStorageService;

/**
 * LocalStorageServiceImpl 本地图片上传实现
 *
 * @author ren
 * @version 2025/06/27 11:22
 **/
@Service
public class LocalStorageServiceImpl implements LocalStorageService {

    @Autowired
    private ImageLogMapper imageLogMapper;

    /**
     * 以默认配置进行文件上传
     *
     * @param file 上传的文件
     * @return 文件名称
     * @throws Exception
     */
    @Override
    public String uploadV1(MultipartFile file) throws IOException {
        try {
            return uploadV3(FileUploadUtils.getDefaultBaseDir(), file, MimeTypeConstants.DEFAULT_ALLOWED_EXTENSION);
        } catch (Exception e) {
            throw new IOException(e.getMessage(), e);
        }
    }

    /**
     * 根据文件路径上传
     *
     * @param baseDir 相对应用的基目录
     * @param file 上传的文件
     * @return 文件名称
     * @throws IOException
     */
    @Override
    public String uploadV2(String baseDir, MultipartFile file) throws IOException {
        try {
            return uploadV3(baseDir, file, MimeTypeConstants.DEFAULT_ALLOWED_EXTENSION);
        } catch (Exception e) {
            throw new IOException(e.getMessage(), e);
        }
    }

    /**
     * 文件上传
     *
     * @param baseDir 相对应用的基目录
     * @param file 上传的文件
     * @param allowedExtension 上传文件类型
     * @return 返回上传成功的文件名
     * @throws FileSizeLimitExceededException 如果超出最大大小
     * @throws FileNameLengthLimitExceededException 文件名太长
     * @throws IOException 比如读写文件出错时
     * @throws InvalidExtensionException 文件校验异常
     */
    @Override
    public String uploadV3(String baseDir, MultipartFile file, String[] allowedExtension)
        throws FileSizeLimitExceededException, IOException, FileNameLengthLimitExceededException,
        InvalidExtensionException {
        return uploadV4(baseDir, file, allowedExtension, true);
    }

    /**
     * 文件上传
     *
     * @param baseDir 相对应用的基目录
     * @param file 上传的文件
     * @param useCustomNaming 系统自定义文件名（true表示使用UUID自定义文件名，false表示使用包含原本贱名的新文件名）
     * @param allowedExtension 上传文件类型
     * @return 返回上传成功的文件名
     * @throws FileSizeLimitExceededException 如果超出最大大小
     * @throws FileNameLengthLimitExceededException 文件名太长
     * @throws IOException 比如读写文件出错时
     * @throws InvalidExtensionException 文件校验异常
     */
    @Override
    public String uploadV4(String baseDir, MultipartFile file, String[] allowedExtension, boolean useCustomNaming)
        throws FileSizeLimitExceededException, IOException, FileNameLengthLimitExceededException,
        InvalidExtensionException {
        int fileNameLength = Objects.requireNonNull(file.getOriginalFilename()).length();
        if (fileNameLength > FileUploadUtils.DEFAULT_FILE_NAME_LENGTH) {
            throw new FileNameLengthLimitExceededException(FileUploadUtils.DEFAULT_FILE_NAME_LENGTH);
        }
        // 文件大小校验
        FileUploadUtils.assertAllowed(file, allowedExtension);
        // 新文件名
        String fileName = useCustomNaming ? FileUploadUtils.uuidFilename(file) : FileUploadUtils.extractFilename(file);
        // 获取最终上传路径
        String absPath = FileUploadUtils.getAbsoluteFile(baseDir, fileName).getAbsolutePath();
        // 文件复制
        file.transferTo(Paths.get(absPath));
        // 获取返回给前端的不包含基础路径的上传路径
        String lastFilePath = FileUploadUtils.getPathFileName(baseDir, fileName);

        // 创建图片日志记录
        createImageLog(file, lastFilePath, StringUtils.subAfter(baseDir, "/", true));
        return lastFilePath;
    }

    /**
     * 创建图片日志记录
     * 
     * @param fileName 存储在OSS中的文件名
     * @param belong 业务归属分类
     * @return 完整的图片日志实体
     */
    private ImageLog createImageLog(MultipartFile file, String fileName, String belong) {
        ImageLog imageLog = new ImageLog();
        imageLog.setBucket("");
        imageLog.setEtag("");
        imageLog.setFileSize(file.getSize());
        imageLog.setMimeType(file.getContentType());
        imageLog.setImageUrl(ServletUtils.getUrl() + fileName);
        imageLog.setName(fileName);
        imageLog.setCloudName("local");
        imageLog.setBelong(belong);
        imageLog.setCreateTime(DateUtils.currentSeconds());
        imageLogMapper.insertImageLog(imageLog);
        return imageLog;
    }

}
