package com.ren.cloudstorage.service;

import java.net.URL;

import org.springframework.web.multipart.MultipartFile;

import com.ren.cloudstorage.domain.entity.CloudImageLog;
import com.ren.cloudstorage.domain.exception.OSSException;

public interface CloudStorageService {

	/**
	 * 获取服务类型
	 * @return java.lang.String
	 * @author ren
	 * @date 2025/06/20 10:23
	 */
	String getServiceType() throws OSSException;

	/**
     * 上传字节数组到云存储
     *
     * @param file 文件
     * @param belong 文件业务归属分类
     * @return 上传后的图片记录信息
     * @throws OSSException 上传过程中发生的异常
     */
    CloudImageLog upload(MultipartFile file, String belong, boolean useCustomNaming) throws OSSException;

	/**
     * 上传字节数组并设置为直接下载模式
     *
     * @param file 文件
     * @param belong 文件业务归属分类
     * @return 上传后的图片记录信息
     * @throws OSSException 上传过程中发生的异常
     */
    CloudImageLog uploadForDirectDownload(MultipartFile file, String belong, boolean useCustomNaming) throws OSSException;

	/**
     * 通过输入流上传文件
     *
     * @param file 文件
     * @param belong 文件业务归属分类
     * @return 上传后的图片记录信息
     * @throws OSSException 上传过程中发生的异常
     */
    CloudImageLog uploadForInputStream(MultipartFile file, String belong, boolean useCustomNaming) throws OSSException;

	/**
	 * 生成文件预览URL
	 *
	 * @param filePath      文件在存储桶中的路径
	 * @return 带签名的预览URL
	 * @throws OSSException 生成过程中发生的异常
	 */
	URL generatePreviewUrl(String filePath) throws OSSException;

}
