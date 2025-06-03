package com.ren.framework.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Mybatis支持*匹配扫描包
 *
 * @author ren
 */
@Configuration
public class MyBatisPlusConfig
{
    /*
     * 配置分页插件
     * @return com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor
     * @author ren
     * @date 2025/05/06 10:33
     */
	@Bean
	public MybatisPlusInterceptor mybatisPlusInterceptor() {
		MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
		// 添加分页插件并指定数据库类型（如 MySQL）
		interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
		return interceptor;
	}

}