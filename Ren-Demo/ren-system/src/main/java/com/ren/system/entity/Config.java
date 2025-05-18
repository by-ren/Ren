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
@TableName("sys_dict_data")
public class DictData extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @TableId(value = "dict_data_id", type = IdType.AUTO)
    private Long dictDataId;
    /**字典顺序*/
    @TableField(value = "dict_sort")
    private Integer dictSort;
    /**字典编码*/
    @TableField(value = "dict_label")
    private String dictLabel;
    /**字典名称*/
    @TableField(value = "dict_value")
    private String dictValue;
    /**字典编码*/
    @TableField(value = "dict_type")
    private String dictType;
    /**字典编码*/
    @TableField(value = "css_class")
    private String cssClass;
    /**字典编码*/
    @TableField(value = "list_class")
    private String listClass;
    /**是否默认（1：是，0：否）*/
    @TableField(value = "is_default")
    private Byte isDefault;
    /**是否停用（1：是，0：否）*/
    @TableField(value = "is_stop")
    private Byte isStop;

}