package com.ren.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_dept_selection")
public class DeptSelection {
    private static final long serialVersionUID = 1L;

    /**部门ID*/
    @TableField(value = "dept_id")
    private Long deptId;
    /**可查看数据部门ID*/
    @TableField(value = "can_viewed_dept_id")
    private Long canViewedDeptId;
}