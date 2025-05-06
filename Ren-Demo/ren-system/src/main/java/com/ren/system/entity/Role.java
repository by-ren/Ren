package com.ren.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ren.common.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_role")
public class Role extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @TableId(value = "role_id", type = IdType.AUTO)
    private Long roleId;
    /**角色名称*/
    @TableField(value = "role_name")
    private String roleName;
    /**是否停用（0否 1是）*/
    @TableField(value = "is_stop")
    private Byte isStop;
    /**角色权限字符串*/
    @TableField(value = "role_sort")
    private String roleKey;
    /**显示顺序*/
    @TableField(value = "role_sort")
    private Integer roleSort;
    /**是否删除*/
    @TableField(value = "is_del")
    private Byte isDel;
}
