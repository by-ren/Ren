package com.ren.framework.config;

import java.util.concurrent.TimeUnit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.ren.common.core.constant.Constants;
import com.ren.common.properties.LocalStorageProperties;
import org.springframework.web.servlet.resource.WebJarsResourceResolver;

/**
 * 通用配置
 * 
 * @author ren
 */
@Configuration
public class ResourcesConfig implements WebMvcConfigurer
{

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 本地文件上传路径 (保持不变)
        // addResourceHandler 当请求路径匹配 /profile/** 模式时，由该处理器处理
        // addResourceLocations 物理资源定位,指定实际文件位置
        // setCacheControl 缓存控制,设置响应头：Cache-Control: public, max-age=18000（5小时缓存）,减少重复请求，提升加载速度
        registry.addResourceHandler(Constants.RESOURCE_PREFIX + "/**")
                .addResourceLocations("file:" + LocalStorageProperties.getProfile() + "/");

        // Swagger UI资源路径
        // registry.addResourceHandler("/swagger-ui/**")
        // .addResourceLocations("classpath:/META-INF/resources/webjars/swagger-ui/5.13.0/")
        // .setCacheControl(CacheControl.maxAge(5, TimeUnit.HOURS).cachePublic());
        // 老版本的swagger-ui没有版本号这个文件夹，但是新版的swagger-ui多了版本号这个文件夹，所以这里要加上版本号
        // 但是直接向上面一样加上版本号不优雅，每次更新Jar都还要去查阅Jar的版本
        // 所以使用下面这个方式自动解析版本，自动解析版本依赖于webjars-locator-core包一定要注意引入
        registry.addResourceHandler("/swagger-ui/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/swagger-ui/")
                .setCacheControl(CacheControl.maxAge(5, TimeUnit.HOURS).cachePublic())
                .resourceChain(true)
                .addResolver(new WebJarsResourceResolver()); // 关键：自动解析版本

        // 添加OpenAPI JSON端点资源路径
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/")
            .setCacheControl(CacheControl.maxAge(1, TimeUnit.HOURS));
    }

    /**
     * 跨域配置
     */
    @Bean
    public CorsFilter corsFilter()
    {
        CorsConfiguration config = new CorsConfiguration();
        // 设置访问源地址
        config.addAllowedOriginPattern("*");
        // 设置访问源请求头
        config.addAllowedHeader("*");
        // 设置访问源请求方法
        config.addAllowedMethod("*");
        // 有效期 1800秒
        config.setMaxAge(1800L);
        // 添加映射路径，拦截一切请求
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        // 返回新的CorsFilter
        return new CorsFilter(source);
    }
}