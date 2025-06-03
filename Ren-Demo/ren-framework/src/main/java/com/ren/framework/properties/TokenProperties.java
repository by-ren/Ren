package com.ren.framework.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component // 添加组件注解
@ConfigurationProperties(prefix = "token")
public class TokenProperties {
    // AccessToken配置
    /**accessToken加密密钥*/
    private String secret;
    /**accessToken有效期：默认值30分钟*/
    private int expireTime = 30 * 60;
    /**剩余多久触发刷新：默认值5分钟*/
    private int refreshTime = 5 * 60;

    // RefreshToken配置
    /**refreshToken加密密钥*/
    private String refreshSecret;
    /**refreshToken有效期：默认值7天*/
    private int refreshExpireTime = 7 * 24 * 60 * 60;

    // blackList配置
    /**accessToken加入黑名单后，黑名单持续时间*/
    private int blackListTime;

    //登陆相关
    /**是否开启单设备登录*/
    private int isOpenSingleDeviceLogin;
}