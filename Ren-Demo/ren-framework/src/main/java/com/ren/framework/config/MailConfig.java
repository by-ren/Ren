package com.ren.framework.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 用于读取mail.yml配置文件（用于Hutool发送邮件时使用，其实本可以直接使用Hutool默认指定的配置文件路径，写一个mail.setting文件即可直接读取，但是这里为了统一，所以选择了自定义配置文件，自己读取）
 *
 * @author ren
 */
@Configuration
@ConfigurationProperties(prefix = "mail")
@Data
public class MailConfig {
	private String host;
	private int port;
	private String username;
	private String password;
	private String from;
	private boolean sslEnable;

}