package com.ren.cloudstorage.properties;

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
@ConfigurationProperties(prefix = "cloud.storage")
@PropertySource(value = { "classpath:/config/project/cloud-storage.yml" })
public class CloudStorageProperties {

	/** 云存储厂商名称 */
	private String vendor;

	public String getVendor() {
		return vendor;
	}

	//注意：因为该配置类所对应的配置文件违背SpringBoot的默认规则，所以需要使用@Value注解进行赋值
	@Value("${vendor}")
	public void setVendor(String vendor) {
		this.vendor = vendor;
	}
}