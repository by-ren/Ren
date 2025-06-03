package com.ren.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_user_role")
public class UserRole {
    private static final long serialVersionUID = 1L;

    /** 用户ID */
    @TableField(value = "user_id")
    private Long userId;
    /** 角色ID */
    @TableField(value = "role_id")
    private Long roleId;

}