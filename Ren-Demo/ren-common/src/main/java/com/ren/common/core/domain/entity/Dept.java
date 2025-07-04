package com.ren.common.core.domain.entity;

import java.util.ArrayList;
import java.util.List;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_dept")
public class Dept extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 部门ID */
    @TableId(value = "dept_id", type = IdType.AUTO)
    private Long deptId;

    /** 父部门ID */
    @TableField(value = "parent_id")
    private Long parentId;

    /** 祖级列表 */
    private String ancestors;

    /** 部门名称 */
    @TableField(value = "dept_name")
    private String deptName;

    /** 显示顺序 */
    @TableField(value = "order_num")
    private Integer orderNum;

    /** 负责人 */
    private String leader;

    /** 联系电话 */
    private String phone;

    /** 邮箱 */
    private String email;

    /** 是否停用（0：否 1：是） */
    @TableField(value = "is_stop")
    private Byte isStop;

    /** 是否删除（0：否 1：是） */
    @TableField(value = "is_del")
    private Byte isDel;

    /*==================================================以下为冗余字段===================================================*/

    /** 父部门名称 */
    @TableField(exist = false)
    private String parentName;

    /** 子部门 */
    @TableField(exist = false)
    private List<Dept> children = new ArrayList<Dept>();

}
