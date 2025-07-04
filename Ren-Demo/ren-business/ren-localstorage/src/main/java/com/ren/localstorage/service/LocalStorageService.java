package com.ren.localstorage.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.ren.common.core.exception.file.FileNameLengthLimitExceededException;
import com.ren.common.core.exception.file.FileSizeLimitExceededException;
import com.ren.common.core.exception.file.InvalidExtensionException;

/**
 * ImageLogService 本地图片上传接口
 *
 * @author ren
 * @version 2025/06/27 11:20
 **/
public interface LocalStorageService {

    /**
     * 以默认配置进行文件上传
     *
     * @param file 上传的文件
     * @return 文件名称
     * @throws Exception
     */
    String uploadV1(MultipartFile file) throws IOException;

    /**
     * 根据文件路径上传
     *
     * @param baseDir 相对应用的基目录
     * @param file 上传的文件
     * @return 文件名称
     * @throws IOException
     */
    String uploadV2(String baseDir, MultipartFile file) throws IOException;

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
    String uploadV3(String baseDir, MultipartFile file, String[] allowedExtension)
        throws FileSizeLimitExceededException, IOException, FileNameLengthLimitExceededException,
        InvalidExtensionException;

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
    String uploadV4(String baseDir, MultipartFile file, String[] allowedExtension, boolean useCustomNaming)
        throws FileSizeLimitExceededException, IOException, FileNameLengthLimitExceededException,
        InvalidExtensionException;

}