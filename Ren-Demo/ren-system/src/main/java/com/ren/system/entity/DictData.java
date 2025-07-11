package com.ren.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ren.common.core.domain.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_dict_data")
public class DictData extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 字典编码 */
    @TableId(value = "dict_data_id", type = IdType.AUTO)
    private Long dictDataId;
    /** 字典排序 */
    @TableField(value = "dict_sort")
    private Integer dictSort;
    /** 字典标签 */
    @TableField(value = "dict_label")
    private String dictLabel;
    /** 字典键值 */
    @TableField(value = "dict_value")
    private String dictValue;
    /** 字典类型 */
    @TableField(value = "dict_type")
    private String dictType;
    /** 样式属性（其他样式扩展） */
    @TableField(value = "css_class")
    private String cssClass;
    /** 表格回显样式 */
    @TableField(value = "list_class")
    private String listClass;
    /** 是否默认（1：是，0：否） */
    @TableField(value = "is_default")
    private Byte isDefault;
    /** 是否停用（1：是，0：否） */
    @TableField(value = "is_stop")
    private Byte isStop;

}