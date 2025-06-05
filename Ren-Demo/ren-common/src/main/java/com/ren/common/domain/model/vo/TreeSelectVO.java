package com.ren.common.domain.model.vo;

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
 * Treeselect树结构实体类
 *
 * @author ren
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TreeSelectVO implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 节点ID */
    private Long id;

    /** 节点名称 */
    private String label;

    /** 节点禁用 */
    private boolean disabled = false;

    /** 子节点 */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<TreeSelectVO> children;

    /*
     * 将传进来的普通树形结构转换为下拉框树形结构
     * @param t
     * @param idFieldName
     * @param labelFieldName
     * @param disabledFieldName
     * @param childrenFieldName
     * @author ren
     * @date 2025/05/09 22:35
     */
    public <T> TreeSelectVO(
            T t,
            String idFieldName,
            String labelFieldName,
            String disabledFieldName,
            String childrenFieldName
    ) {

        // 通过 BeanUtil 动态根据属性名获取属性值（获取主键值）
        Object idInline = BeanUtil.getProperty(t, idFieldName);
        // 如果获取不到相关内容，则报错
        if(idInline == null){
            throw new RuntimeException("所传入的idFieldName字段名为无效值");
        }
        // 判断获取到的内容是不是Long类型，如果不是则报错
        if (!(idInline instanceof Long)) {
            throw new IllegalArgumentException("idInline 必须对应 Long 类型属性");
        }
        this.id = Convert.toLong(idInline);

        // 通过 BeanUtil 动态根据属性名获取属性值（获取名称值）
        Object labelInline = BeanUtil.getProperty(t, labelFieldName);
        // 如果获取不到相关内容，则报错
        if(labelInline == null){
            throw new RuntimeException("所传入的labelFieldName字段名为无效值");
        }
        // 判断获取到的内容是不是String类型，如果不是则报错
        if (!(labelInline instanceof String)) {
            throw new IllegalArgumentException("labelInline 必须对应 String 类型属性");
        }
        this.label = Convert.toStr(labelInline);

        // 通过 BeanUtil 动态根据属性名获取属性值（获取是否禁用值，之后判断是否禁用是否等于1，等于一则为禁用，否则为不禁用）
        Object disabledInline = BeanUtil.getProperty(t, disabledFieldName);
        // 如果获取不到相关内容，则报错
        if(disabledInline == null){
            throw new RuntimeException("所传入的disabledFieldName字段名为无效值");
        }
        // 判断获取到的内容是不是Byte类型，如果不是则报错
        if (!(disabledInline instanceof Byte)) {
            throw new IllegalArgumentException("disabledInline 必须对应 Byte 类型属性");
        }
        this.disabled = Convert.toByte(disabledInline) == 1;

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
                .map(child -> new TreeSelectVO(
                        child,
                        idFieldName,
                        labelFieldName,
                        disabledFieldName,
                        childrenFieldName
                ))
                .collect(Collectors.toList());
    }

}
