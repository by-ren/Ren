package com.ren.common.core.domain.entity;

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
@TableName("sys_role")
public class Role extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 角色ID */
    @TableId(value = "role_id", type = IdType.AUTO)
    private Long roleId;
    /** 角色名称 */
    @TableField(value = "role_name")
    private String roleName;
    /** 是否停用（0否 1是） */
    @TableField(value = "is_stop")
    private Byte isStop;
    /** 角色权限字符串 */
    @TableField(value = "role_key")
    private String roleKey;
    /** 显示顺序 */
    @TableField(value = "role_sort")
    private Integer roleSort;
    /** 是否删除 */
    @TableField(value = "is_del")
    private Byte isDel;
    /** 可查看数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限 5：仅本人数据权限） */
    @TableField(value = "data_scope")
    private Byte dataScope;


    /*================================================以下为冗余字段=====================================================*/
    /** 菜单ID数组 */
    @TableField(exist = false)
    private Long[] menuIds;
    /** 部门ID数组 */
    @TableField(exist = false)
    private Long[] deptIds;
}
