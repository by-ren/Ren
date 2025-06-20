package com.ren.cloudstorage.utils;

import java.util.UUID;

public class FileUtils {

	/**
	 * 生成唯一的存储文件名
	 *
	 * 格式：归属分类目录/UUID.扩展名
	 *
	 * @param belong 业务归属分类
	 * @param originalName 原始文件名
	 * @return 生成的新文件名
	 */
	public static String getFileNameHex(String belong, String originalName) {
		String extension = getFileExtension(originalName);
		String uuid = UUID.randomUUID().toString().replace("-", "");
		return String.format("%s/%s.%s", belong, uuid, extension);
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
	 * 获取文件扩展名
	 *
	 * @param filename 文件名
	 * @return 文件扩展名（不带点）
	 */
	private static String getFileExtension(String filename) {
		if (filename == null || filename.isEmpty()) {
			return "";
		}
		int dotIndex = filename.lastIndexOf('.');
		return (dotIndex == -1) ? "" : filename.substring(dotIndex + 1);
	}

	/**
	 * 获取文件名称 /profile/upload/2022/04/16/ruoyi.png -- ruoyi.png
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
