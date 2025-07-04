package com.ren.common.core.domain.vo;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 动态路由实体类
 *
 * @author ren
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

    private List<DynamicRouteVO> children;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class Meta{
        private Boolean requiresAuth;
        private String[] roles;
        private String menuShow;

        public Meta(Meta meta) {
            this.requiresAuth = meta.getRequiresAuth();
            this.roles = meta.getRoles();
            this.menuShow = meta.getMenuShow();
        }
    }

    public <T> DynamicRouteVO(
            T t,
            String pathFieldName,
            String nameFieldName,
            String componentFieldName,
            Meta meta,
            String childrenFieldName
    ) {

        // 通过 BeanUtil 动态根据属性名获取属性值（获取Index值）
        Object pathInline = BeanUtil.getProperty(t, pathFieldName);
        // 如果获取不到相关内容，则报错
        if(pathInline == null){
            throw new RuntimeException("所传入的pathFieldName字段名为无效值");
        }
        // 判断获取到的内容是不是String类型，如果不是则报错
        if (!(pathInline instanceof String)) {
            throw new IllegalArgumentException("pathInline 必须对应 String 类型属性");
        }
        this.path = Convert.toStr(pathInline);

        // 通过 BeanUtil 动态根据属性名获取属性值（获取名称值）
        Object nameInline = BeanUtil.getProperty(t, nameFieldName);
        // 如果获取不到相关内容，则报错
        if(nameInline == null){
            throw new RuntimeException("所传入的nameFieldName字段名为无效值");
        }
        // 判断获取到的内容是不是String类型，如果不是则报错
        if (!(nameInline instanceof String)) {
            throw new IllegalArgumentException("nameInline 必须对应 String 类型属性");
        }
        this.name = Convert.toStr(nameInline);

        // 通过 BeanUtil 动态根据属性名获取属性值（获取图标值）
        Object componentInline = BeanUtil.getProperty(t, componentFieldName);
        // 如果获取不到相关内容，则报错
        if(componentInline == null){
            throw new RuntimeException("所传入的componentFieldName字段名为无效值");
        }
        // 判断获取到的内容是不是String类型，如果不是则报错
        if (!(componentInline instanceof String)) {
            throw new IllegalArgumentException("componentInline 必须对应 String 类型属性");
        }
        this.component = Convert.toStr(componentInline);

        //实现深拷贝，防止所有对象使用同一个Meta元素
        this.meta = new Meta(meta);

        // 通过 BeanUtil 动态根据属性名获取属性值（获取子列表值）
        Object rawChildList = BeanUtil.getProperty(t, childrenFieldName);
        // 如果获取不到相关内容，则报错
        if (rawChildList == null) {
            throw new RuntimeException("所传入的childrenFieldName字段名为无效值");
        }
        // 判断子列表是不是List结构，如果不是则报错
        if (!(rawChildList instanceof List<?>)) {
            throw new IllegalArgumentException("childrenFieldName 必须对应 List 类型属性");
        }
        // 递归调用该方法，构建子元素树形结构
        this.children = ((List<T>) rawChildList).stream()
                .map(child -> new DynamicRouteVO(
                        child,
                        pathFieldName,
                        nameFieldName,
                        componentFieldName,
                        meta,
                        childrenFieldName
                ))
                .collect(Collectors.toList());
    }

}