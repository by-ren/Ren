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
@TableName("sys_config")
public class Config extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 参数主键 */
    @TableId(value = "config_id", type = IdType.AUTO)
    private Long configId;
    /** 参数名称 */
    @TableField(value = "config_name")
    private String configName;
    /** 参数键名 */
    @TableField(value = "config_key")
    private String configKey;
    /** 参数键值 */
    @TableField(value = "config_value")
    private String configValue;
    /** 是否系统内置（1：是，0：否） */
    @TableField(value = "is_system")
    private Byte isSystem;

}