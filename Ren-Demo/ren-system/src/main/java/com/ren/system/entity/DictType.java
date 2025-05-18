package com.ren.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ren.common.domain.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_dict_type")
public class DictType extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @TableId(value = "dict_type_id", type = IdType.AUTO)
    private Long dictTypeId;
    /**字典名称*/
    @TableField(value = "dict_name")
    private String dictName;
    /**字典编码*/
    @TableField(value = "dict_code")
    private String dictCode;
    /**是否停用（1：是，0：否）*/
    @TableField(value = "is_stop")
    private Byte isStop;

}