package com.ren.common.core.domain.vo;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * MenuVO树结构实体类
 *
 * @author ren
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuVO implements Serializable
{
    private static final long serialVersionUID = 1L;

    private String index;

    private String name;
    
    private String icon;

    /** 子节点 */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<MenuVO> children;

    /*
     * 将传进来的普通树形结构转换为下拉框树形结构
     * @param t
     * @param indexFieldName
     * @param nameFieldName
     * @param iconFieldName
     * @param childrenFieldName
     * @author ren
     * @date 2025/05/09 22:35
     */
    public <T> MenuVO(
            T t,
            String indexFieldName,
            String nameFieldName,
            String iconFieldName,
            String childrenFieldName
    ) {

        // 通过 BeanUtil 动态根据属性名获取属性值（获取Index值）
        Object indexInline = BeanUtil.getProperty(t, indexFieldName);
        // 如果获取不到相关内容，则报错
        if(indexInline == null){
            throw new RuntimeException("所传入的indexFieldName字段名为无效值");
        }
        // 判断获取到的内容是不是String类型，如果不是则报错
        if (!(indexInline instanceof String)) {
            throw new IllegalArgumentException("indexInline 必须对应 String 类型属性");
        }
        this.index = Convert.toStr(indexInline);

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
        Object iconInline = BeanUtil.getProperty(t, iconFieldName);
        // 如果获取不到相关内容，则报错
        if(iconInline == null){
            throw new RuntimeException("所传入的iconFieldName字段名为无效值");
        }
        // 判断获取到的内容是不是String类型，如果不是则报错
        if (!(iconInline instanceof String)) {
            throw new IllegalArgumentException("iconInline 必须对应 String 类型属性");
        }
        this.icon = Convert.toStr(iconInline);

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
                .map(child -> new MenuVO(
                        child,
                        indexFieldName,
                        nameFieldName,
                        iconFieldName,
                        childrenFieldName
                ))
                .collect(Collectors.toList());
    }

}
