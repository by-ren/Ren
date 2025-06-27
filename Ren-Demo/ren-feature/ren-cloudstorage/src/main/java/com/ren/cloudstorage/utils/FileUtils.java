package com.ren.cloudstorage.utils;

import java.util.Objects;

import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import com.ren.cloudstorage.domain.constant.MimeTypeConstants;
import com.ren.cloudstorage.utils.uuid.IdUtils;
import com.ren.cloudstorage.utils.uuid.Seq;

public class FileUtils {

	/**
     * 编码文件名(日期格式目录 + 原文件名 + 序列值 + 后缀)
     * 
     * @param belong 业务归属分类
     * @param file 文件
     * @author ren
     * @date 2025/06/27 10:10
     */
    public static final String extractFilename(String belong, MultipartFile file) {
        return StringUtils.format("{}/{}/{}_{}.{}", belong, DateUtils.datePath(),
            FilenameUtils.getBaseName(file.getOriginalFilename()), Seq.getId(Seq.uploadSeqType), getExtension(file));
    }

    /**
     * 编编码文件名(日期格式目录 + UUID + 后缀)
     * 
     * @param belong 业务归属分类
     * @param file 文件
     * @author ren
     * @date 2025/06/27 10:10
     */
    public static final String uuidFilename(String belong, MultipartFile file) {
        return StringUtils.format("{}/{}/{}.{}", belong, DateUtils.datePath(), IdUtils.fastSimpleUUID(),
            getExtension(file));
	}

	/**
	 * 生成完整的文件访问URL
	 *
	 * @param endpoint OSS端点地址
	 * @param fileName 存储在OSS中的文件名
	 * @return 完整的访问URL
	 */
	public static String getImageUrl(String endpoint, String fileName) {
		return String.format("%s/%s", endpoint, fileName);
	}

	/**
     * 获取文件名的后缀
     *
     * @param file 表单文件
     * @return 后缀名
     */
    public static final String getExtension(MultipartFile file) {
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        if (StringUtils.isEmpty(extension)) {
            extension = MimeTypeConstants.getExtension(Objects.requireNonNull(file.getContentType()));
		}
        return extension;
	}

	/**
	 * 获取文件名称 /profile/upload/2022/04/16/ren.png -- ren.png
	 *
	 * @param fileName 路径名称
	 * @return 没有文件路径的名称
	 */
	public static String getName(String fileName)
	{
		if (fileName == null)
		{
			return null;
		}
		int lastUnixPos = fileName.lastIndexOf('/');
		int lastWindowsPos = fileName.lastIndexOf('\\');
		int index = Math.max(lastUnixPos, lastWindowsPos);
		return fileName.substring(index + 1);
	}

}
