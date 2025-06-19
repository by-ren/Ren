
package com.ren.common.utils;

import java.util.UUID;

/**
 * File 工具类
 */
public final class FileUtils {

    // 禁止实例化
    private FileUtils() {
        throw new AssertionError("不允许实例化工具类");
    }


    /**
     * 字节转换
     *
     * @param size 字节大小
     * @return 转换后值
     */
    public static String convertFileSize(long size)
    {
        long kb = 1024;
        long mb = kb * 1024;
        long gb = mb * 1024;
        if (size >= gb)
        {
            return String.format("%.1f GB", (float) size / gb);
        }
        else if (size >= mb)
        {
            float f = (float) size / mb;
            return String.format(f > 100 ? "%.0f MB" : "%.1f MB", f);
        }
        else if (size >= kb)
        {
            float f = (float) size / kb;
            return String.format(f > 100 ? "%.0f KB" : "%.1f KB", f);
        }
        else
        {
            return String.format("%d B", size);
        }
    }

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
        return String.format("https://%s/%s", endpoint, fileName);
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

}