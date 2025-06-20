package com.ren.cloudstorage.service.aliyun;

import cn.hutool.core.date.DateUtil;
import com.aliyun.oss.HttpMethod;
import com.ren.cloudstorage.domain.entity.ImageLog;
import com.ren.cloudstorage.domain.enums.OSSReturnCodeEnum;
import com.ren.cloudstorage.domain.exception.OSSException;
import com.ren.cloudstorage.mapper.ImageLogMapper;
import com.ren.cloudstorage.utils.FileUtils;
import com.ren.cloudstorage.config.AliyunConfig;
import org.springframework.beans.factory.annotation.Autowired;

import com.aliyun.oss.OSS;
import com.aliyun.oss.model.*;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;

@Service
public class AliyunCloudStorageServiceImpl implements AliyunCloudStorageService {

	private final OSS ossClient;
	private final AliyunConfig aLiYunConfig;
	private final ImageLogMapper imageLogMapper;

	@Autowired
	public AliyunCloudStorageServiceImpl(OSS ossClient, AliyunConfig aLiYunConfig, ImageLogMapper imageLogMapper) {
		this.ossClient = ossClient;
		this.aLiYunConfig = aLiYunConfig;
		this.imageLogMapper = imageLogMapper;
	}

	/**
	 * 获取服务类型
	 * @return java.lang.String
	 * @author ren
	 * @date 2025/06/20 10:23
	 */
	@Override
	public String getServiceType() throws OSSException {
		return "aliyun";
	}

	/**
	 * 标准上传实现：上传字节数组到OSS
	 * <p>
	 * 流程：
	 * 1. 验证输入数据非空
	 * 2. 生成唯一文件名（belong目录+UUID）
	 * 3. 上传到OSS
	 * 4. 获取文件元数据
	 * 5. 创建图片记录并存入数据库
	 */
	@Override
	public ImageLog upload(byte[] data, String belong, String sourceFileName) throws OSSException {
		Assert.notNull(data, "上传数据不能为空");
		String fileName = FileUtils.getFileNameHex(belong, sourceFileName);

		try {
			ossClient.putObject(aLiYunConfig.getBucketName(), fileName, new ByteArrayInputStream(data));
			return createImageLog(fileName, belong, ossClient.getObjectMetadata(aLiYunConfig.getBucketName(), fileName));
		} catch (Exception e) {
			throw new OSSException(OSSReturnCodeEnum.UPLOAD_ERROR, e);
		}
	}

	/**
	 * 直接下载模式上传：设置content-type为二进制流
	 * <p>
	 * 特殊处理：
	 * 1. 使用PutObjectRequest显式设置元数据
	 * 2. 设置content-type为application/octet-stream
	 * 3. 确保浏览器下载而不是预览
	 */
	@Override
	public ImageLog uploadForDirectDownload(byte[] data, String belong, String sourceFileName) throws OSSException {
		Assert.notNull(data, "上传数据不能为空");
		String fileName = FileUtils.getFileNameHex(belong, sourceFileName);

		try {
			PutObjectRequest request = new PutObjectRequest(aLiYunConfig.getBucketName(), fileName, new ByteArrayInputStream(data));

			ObjectMetadata metadata = new ObjectMetadata();
			metadata.setContentType("application/octet-stream");
			request.setMetadata(metadata);

			ossClient.putObject(request);
			return createImageLog(fileName, belong, metadata);
		} catch (Exception e) {
			throw new OSSException(OSSReturnCodeEnum.UPLOAD_ERROR, e);
		}
	}

	/**
	 * 流式上传实现：处理大文件或未知长度的流
	 * <p>
	 * 注意：
	 * 1. 输入流由调用方负责关闭
	 * 2. 使用OSS的getObjectMetadata获取元数据
	 */
	@Override
	public ImageLog upload(InputStream inputStream, String belong, String sourceFileName) throws OSSException {
		Assert.notNull(inputStream, "输入流不能为空");
		String fileName = FileUtils.getFileNameHex(belong, sourceFileName);

		try {
			ossClient.putObject(aLiYunConfig.getBucketName(), fileName, inputStream);
			return createImageLog(fileName, belong, ossClient.getObjectMetadata(aLiYunConfig.getBucketName(), fileName));
		} catch (Exception e) {
			throw new OSSException(OSSReturnCodeEnum.UPLOAD_ERROR, e);
		}
	}

	/**
	 * 生成文件预览URL
	 * <p>
	 * 特点：
	 * 1. 设置有效期为10分钟
	 * 2. 使用imm/previewdoc处理文档预览
	 * 3. 支持Word、Excel、PDF等格式预览
	 */
	@Override
	public URL generatePreviewUrl(String filePath) throws OSSException {
		try {
			final int PREVIEW_EXPIRATION_MINUTES = 10;
			Date expiration = new Date(System.currentTimeMillis() + PREVIEW_EXPIRATION_MINUTES * 60 * 1000);

			GeneratePresignedUrlRequest req = new GeneratePresignedUrlRequest(aLiYunConfig.getBucketName(), filePath, HttpMethod.GET);
			req.setExpiration(expiration);
			req.setProcess("imm/previewdoc,copy_1");

			return ossClient.generatePresignedUrl(req);
		} catch (Exception e) {
			throw new OSSException(OSSReturnCodeEnum.PREVIEW_GENERATION_ERROR, e);
		}
	}

	/**
	 * 创建图片日志记录
	 * <p>
	 * 记录内容：
	 * 1. 文件基础信息：大小、类型
	 * 2. 位置信息：存储桶、路径
	 * 3. 云服务信息
	 * 4. 业务归属分类
	 *
	 * @param fileName 存储在OSS中的文件名
	 * @param belong   业务归属分类
	 * @param metadata OSS元数据
	 * @return 完整的图片日志实体
	 */
	private ImageLog createImageLog(String fileName, String belong, ObjectMetadata metadata) {
		ImageLog imageLog = new ImageLog();
		imageLog.setBucket(aLiYunConfig.getBucketName());
		imageLog.setEtag(metadata.getETag());
		imageLog.setName(fileName);
		imageLog.setFileSize(metadata.getContentLength());
		imageLog.setMimeType(metadata.getContentType());
		imageLog.setImageUrl(FileUtils.getImageUrl(aLiYunConfig.getImageOssPathRead(), fileName));
		imageLog.setCloudName("aliyun");
		imageLog.setBelong(belong);
		imageLog.setCreateTime(DateUtil.currentSeconds());

		imageLogMapper.insertImageLog(imageLog);
		return imageLog;
	}

}