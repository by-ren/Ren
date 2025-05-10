package com.ren.common.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ren.common.core.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_menu")
public class Menu extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 菜单ID */
    @TableId(value = "menu_id", type = IdType.AUTO)
    private Long menuId;

    /** 菜单名称 */
    @TableField(value = "menu_name")
    private String menuName;

    /** 父菜单ID */
    @TableField(value = "parent_id")
    private Long parentId;

    /** 显示顺序 */
    @TableField(value = "order_num")
    private Integer orderNum;

    /** 路由名称，默认和路由地址相同的驼峰格式（注意：因为vue3版本的router会删除名称相同路由，为避免名字的冲突，特殊情况可以自定义） */
    @TableField(value = "route_name")
    private String routeName;

    /** 路由地址 */
    private String path;

    /** 组件路径 */
    private String component;

    /** 路由参数 */
    private String query;

    /** 是否为外链（0是 1否） */
    @TableField(value = "is_frame")
    private Byte isFrame;

    /** 是否缓存（0缓存 1不缓存） */
    @TableField(value = "is_cache")
    private Byte isCache;

    /** 类型（M目录 C菜单 F按钮） */
    @TableField(value = "menu_type")
    private String menuType;

    /** 是否隐藏（0：否 1：是） */
    @TableField(value = "is_visible")
    private Byte isVisible;

    /** 是否停用（0：否 1：是） */
    @TableField(value = "is_stop")
    private Byte isStop;

    /** 权限字符串 */
    private String perms;

    /** 菜单图标 */
    private String icon;

    /** 是否删除（0：否 1：是） */
    @TableField(value = "is_del")
    private Byte isDel;

    /*==================================================以下为冗余字段===================================================*/

    /** 子菜单 */
    @TableField(exist = false)
    private List<Menu> children = new ArrayList<Menu>();

    /** 父菜单名称 */
    @TableField(exist = false)
    private String parentName;
}
