package com.ren.cloudstorage.service;

import com.ren.cloudstorage.domain.entity.ImageLog;
import com.ren.cloudstorage.domain.exception.OSSException;

import java.io.InputStream;
import java.net.URL;

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
	 * @param data          文件字节数组
	 * @param belong        文件业务归属分类
	 * @param fileName      原始文件名
	 * @return 上传后的图片记录信息
	 * @throws OSSException 上传过程中发生的异常
	 */
	ImageLog upload(byte[] data, String belong, String fileName) throws OSSException;

	/**
	 * 上传字节数组并设置为直接下载模式
	 *
	 * @param data          文件字节数组
	 * @param belong        文件业务归属分类
	 * @param fileName      原始文件名
	 * @return 上传后的图片记录信息
	 * @throws OSSException 上传过程中发生的异常
	 */
	ImageLog uploadForDirectDownload(byte[] data, String belong, String fileName) throws OSSException;

	/**
	 * 通过输入流上传文件
	 *
	 * @param inputStream   文件输入流
	 * @param belong        文件业务归属分类
	 * @param fileName      原始文件名
	 * @return 上传后的图片记录信息
	 * @throws OSSException 上传过程中发生的异常
	 */
	ImageLog upload(InputStream inputStream, String belong, String fileName) throws OSSException;

	/**
	 * 生成文件预览URL
	 *
	 * @param filePath      文件在存储桶中的路径
	 * @return 带签名的预览URL
	 * @throws OSSException 生成过程中发生的异常
	 */
	URL generatePreviewUrl(String filePath) throws OSSException;

}
