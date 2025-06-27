package com.ren.common.properties;

import com.ren.common.utils.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component // 添加组件注解
@ConfigurationProperties(prefix = "localstorage")
public class LocalStorageProperties {

    private static String profile;

    public static String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    /**
     * 获取上传路径
     */
    public static String getUploadPath(String belong)
    {
        return StringUtils.isNotBlank(belong) ? getProfile() + "/" + belong : getProfile();
    }
}