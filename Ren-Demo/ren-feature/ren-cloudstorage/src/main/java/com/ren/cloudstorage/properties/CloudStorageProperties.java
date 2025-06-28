package com.ren.cloudstorage.properties;

import com.ren.cloudstorage.factory.NestedYamlPropertySourceFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 云存储通用配置类
 * @author ren
 * @date 2025/06/20 13:46
 */
@Component
// 1.该注解表示让SpringBoot自动扫描并加载该配置文件（有一个特点，只要该项目中任何一个地方加了该注释，其他配置类都能读取到该配置文件中的配置项）
//	 但是注意，该配置类其实本身是为了properties格式设计的，不支持yaml，所以如果使用该注解导入yaml文件，虽然yaml被加载了，但是根据前缀匹配将会失效，例如@ConfigurationProperties(prefix = "cloud.storage.aliyun")
//	 只能使用@Value("${aliyun-access-key-id}")这种无前缀方式读取，但是这样，如果项目中存在多个同名配置项，就会出问题，所以如果使用这个注解导入yaml，最好自定义一个工厂类，让他可以正确读取yaml
@PropertySource(value = { "classpath:/config/project/cloud-storage.yml" },factory = NestedYamlPropertySourceFactory.class)
@ConfigurationProperties(prefix = "cloud.storage")
public class CloudStorageProperties {

	/** 云存储厂商名称 */
	private String vendor;

	public String getVendor() {
		return vendor;
	}

	@Value("${vendor}")
	public void setVendor(String vendor) {
		this.vendor = vendor;
	}
}