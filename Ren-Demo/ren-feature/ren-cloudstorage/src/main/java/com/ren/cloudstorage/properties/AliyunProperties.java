package com.ren.cloudstorage.properties;

import com.aliyun.oss.ClientBuilderConfiguration;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 阿里云云存储专属配置类
 * @author ren
 * @date 2025/06/20 13:46
 */
@Component
@ConfigurationProperties(prefix = "cloud.storage.vendor.aliyun")
@PropertySource(value = { "classpath:/config/project/cloud-storage.yml" })
public class AliyunProperties
{
	/** 阿里云accessKeyId */
	private String accessKeyId;
	/** 阿里云accessKeySecret */
	private String accessKeySecret;
	/** 阿里云OSS存储空间名称 */
	private String bucketName;
	/** 阿里云云外网域名地址 */
	private String endpoint;
	/** OSS上的bucket中的某个图片文件夹的名称，也就是要上传到的图片文件夹 */
	private String imageUploadPath;
	/** OSS的处理图片的绑定域名，可以在后面加一些规则来显示图片，详情查看OSS API文档。 */
	private String imageOssPathRead;

	public String getAccessKeyId() {
		return accessKeyId;
	}

	//注意：因为该配置类所对应的配置文件违背SpringBoot的默认规则，所以需要使用@Value注解进行赋值
	@Value("${access-key-id}")
	public void setAccessKeyId(String accessKeyId) {
		this.accessKeyId = accessKeyId;
	}

	public String getAccessKeySecret() {
		return accessKeySecret;
	}

	//注意：因为该配置类所对应的配置文件违背SpringBoot的默认规则，所以需要使用@Value注解进行赋值
	@Value("${access-key-secret}")
	public void setAccessKeySecret(String accessKeySecret) {
		this.accessKeySecret = accessKeySecret;
	}

	public String getBucketName() {
		return bucketName;
	}

	//注意：因为该配置类所对应的配置文件违背SpringBoot的默认规则，所以需要使用@Value注解进行赋值
	@Value("${bucket-name}")
	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}

	public String getEndpoint() {
		return endpoint;
	}

	//注意：因为该配置类所对应的配置文件违背SpringBoot的默认规则，所以需要使用@Value注解进行赋值
	@Value("${endpoint}")
	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	public String getImageUploadPath() {
		return imageUploadPath;
	}

	//注意：因为该配置类所对应的配置文件违背SpringBoot的默认规则，所以需要使用@Value注解进行赋值
	@Value("${image-upload-path}")
	public void setImageUploadPath(String imageUploadPath) {
		this.imageUploadPath = imageUploadPath;
	}

	public String getImageOssPathRead() {
		return imageOssPathRead;
	}

	//注意：因为该配置类所对应的配置文件违背SpringBoot的默认规则，所以需要使用@Value注解进行赋值
	@Value("${image_oss_path_read}")
	public void setImageOssPathRead(String imageOssPathRead) {
		this.imageOssPathRead = imageOssPathRead;
	}

	/**
	 * 创建OSS客户端单例Bean
	 *
	 * @return OSS客户端实例
	 *
	 * 配置说明：
	 * 1. 设置Socket超时时间为10分钟
	 * 2. 设置最大错误重试次数为5次
	 * 3. 使用@Bean的destroyMethod确保Spring关闭时自动调用shutdown
	 */
	@Bean(destroyMethod = "shutdown")
	public OSS ossClient() {
		ClientBuilderConfiguration conf = new ClientBuilderConfiguration();
		conf.setSocketTimeout(600_000); // 10分钟超时
		conf.setMaxErrorRetry(5);       // 重试5次

		return new OSSClientBuilder()
				.build(endpoint, accessKeyId, accessKeySecret, conf);
	}

}