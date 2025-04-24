package com.ren.system.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component // 添加组件注解
@ConfigurationProperties(prefix = "token")
public class TokenProperties {
    // AccessToken配置
    private String secret;
    private int expireTime = 30 * 60; // 30分钟
    private int refreshTime = 5 * 60; // 剩余5分钟时触发刷新

    // RefreshToken配置
    private String refreshSecret;
    private int refreshExpireTime = 7 * 24 * 60 * 60; // 7天
}