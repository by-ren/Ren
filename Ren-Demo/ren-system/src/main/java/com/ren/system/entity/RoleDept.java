package com.ren.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_role_dept")
public class RoleDept {
    private static final long serialVersionUID = 1L;

    /**角色ID*/
    @TableField(value = "role_id")
    private Long roleId;
    /**部门ID*/
    @TableField(value = "dept_id")
    private Long deptId;
}