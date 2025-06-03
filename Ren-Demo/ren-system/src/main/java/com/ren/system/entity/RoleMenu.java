package com.ren.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_role_menu")
public class RoleMenu {
    private static final long serialVersionUID = 1L;

    /** 角色ID */
    @TableField(value = "role_id")
    private Long roleId;
    /** 菜单ID */
    @TableField(value = "menu_id")
    private Long menuId;

}