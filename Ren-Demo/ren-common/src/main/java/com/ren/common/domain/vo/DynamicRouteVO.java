package com.ren.common.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;



/**
 * 动态路由实体类
 *
 * @author admin
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DynamicRouteVO implements Serializable
{
    private static final long serialVersionUID = 1L;

    private String path;

    private String name;

    private String component;

    private Meta meta;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class Meta{
        private Boolean requiresAuth;
        private String[] roles;
    }
}