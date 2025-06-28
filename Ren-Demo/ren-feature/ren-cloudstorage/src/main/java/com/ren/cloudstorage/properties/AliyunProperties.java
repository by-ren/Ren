package com.ren.cloudstorage.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import com.aliyun.oss.ClientBuilderConfiguration;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.ren.cloudstorage.factory.NestedYamlPropertySourceFactory;
import com.ren.cloudstorage.utils.StringUtils;

import jakarta.annotation.PostConstruct;

/**
 * 阿里云云存储专属配置类
 * @author ren
 * @date 2025/06/20 13:46
 */
@Component
// 1.该注解表示让SpringBoot自动扫描并加载该配置文件（有一个特点，只要该项目中任何一个地方加了该注释，其他配置类都能读取到该配置文件中的配置项）
// 但是注意，该配置类其实本身是为了properties格式设计的，不支持yaml，所以如果使用该注解导入yaml文件，虽然yaml被加载了，但是根据前缀匹配将会失效，例如@ConfigurationProperties(prefix =
// "cloud.storage.aliyun")
// 只能使用@Value("${aliyun-access-key-id}")这种无前缀方式读取，但是这样，如果项目中存在多个同名配置项，就会出问题，所以如果使用这个注解导入yaml，最好自定义一个工厂类，让他可以正确读取yaml
@PropertySource(value = {"classpath:/config/project/cloud-storage.yml"},
    factory = NestedYamlPropertySourceFactory.class)
public class AliyunProperties
{
	/** 阿里云accessKeyId */
    private String alibabaAccessKeyId;
	/** 阿里云accessKeySecret */
    private String alibabaAccessKeySecret;
	/** 阿里云OSS存储空间名称 */
	private String bucketName;
	/** 阿里云云外网域名地址 */
	private String endpoint;
	/** OSS上的bucket中的某个图片文件夹的名称，也就是要上传到的图片文件夹 */
	private String imageUploadPath;
	/** OSS的处理图片的绑定域名，可以在后面加一些规则来显示图片，详情查看OSS API文档。 */
	private String imageOssPathRead;

    public String getAlibabaAccessKeyId() {
        return alibabaAccessKeyId;
	}

	//因为要读取环境变量，所以使用@Value
    @Value("${cloud.storage.aliyun.alibaba-access-key-id}")
    public void setAlibabaAccessKeyId(String alibabaAccessKeyId) {
        this.alibabaAccessKeyId = alibabaAccessKeyId;
	}

    public String getAlibabaAccessKeySecret() {
        return alibabaAccessKeySecret;
	}

    @Value("${cloud.storage.aliyun.alibaba-access-key-secret}")
    public void setAlibabaAccessKeySecret(String alibabaAccessKeySecret) {
        this.alibabaAccessKeySecret = alibabaAccessKeySecret;
	}

	public String getBucketName() {
		return bucketName;
	}

    @Value("${cloud.storage.aliyun.bucket-name}")
	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}

	public String getEndpoint() {
		return endpoint;
	}

    @Value("${cloud.storage.aliyun.endpoint}")
	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	public String getImageUploadPath() {
		return imageUploadPath;
	}

    @Value("${cloud.storage.aliyun.image-upload-path}")
	public void setImageUploadPath(String imageUploadPath) {
		this.imageUploadPath = imageUploadPath;
	}

	public String getImageOssPathRead() {
		return imageOssPathRead;
	}

    @Value("${cloud.storage.aliyun.image_oss_path_read}")
	public void setImageOssPathRead(String imageOssPathRead) {
		this.imageOssPathRead = imageOssPathRead;
	}

    /**
     * 验证OSS配置是否正确
     * 
     * @author ren
     * @date 2025/06/28 14:34
     */
    @PostConstruct
    public void validateCredentials() {
        // 验证是否正确获取到OSS配置
        if (StringUtils.isBlank(alibabaAccessKeyId) || StringUtils.isBlank(alibabaAccessKeySecret)) {
            throw new SecurityException("阿里云OSS凭证未配置，如不需要上传，请在主模块移除该上传模块!");
        }
        // 格式合规性检查（阿里云标准）
        if (!alibabaAccessKeyId.startsWith("LTAI") || alibabaAccessKeyId.length() < 20) {
            throw new SecurityException("AccessKey ID 格式异常! 应是以LTAI开头的20-30位字符串，如不需要上传，请在主模块移除该上传模块");
        }
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
            .build(endpoint, alibabaAccessKeyId, alibabaAccessKeySecret, conf);
	}

}