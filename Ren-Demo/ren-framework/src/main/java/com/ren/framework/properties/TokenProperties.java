package com.ren.framework.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component // 添加组件注解
@ConfigurationProperties(prefix = "token")
public class TokenProperties {
    // AccessToken配置
    private String secret;            // accessToken加密密钥
    private int expireTime = 30 * 60; // accessToken有效期：默认值30分钟
    private int refreshTime = 5 * 60; // 剩余多久触发刷新：默认值5分钟

    // RefreshToken配置
    private String refreshSecret;     // refreshToken加密密钥
    private int refreshExpireTime = 7 * 24 * 60 * 60; // refreshToken有效期：默认值7天

    // blackList配置
    private int blackListTime;        //  accessToken加入黑名单后，黑名单持续时间
}