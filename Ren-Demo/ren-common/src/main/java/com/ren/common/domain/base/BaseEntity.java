package com.ren.common.domain.base;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class BaseEntity implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 创建者 */
    @TableField(value = "create_by")
    private String createBy;
    /** 创建时间 (秒时间戳) */
    @TableField(value = "create_time")
    private Long createTime;
    /** 更新者 */
    @TableField(value = "update_by")
    private String updateBy;
    /** 更新时间 (秒时间戳) */
    @TableField(value = "update_time")
    private Long updateTime;
    /** 备注 */
    @TableField(value = "remark")
    private String remark;

    /*==================================================以下为冗余字段===================================================*/
    /** 创建时间Str */
    @TableField(exist = false)
    private String createTimeStr;
    /** 更新时间Str */
    @TableField(exist = false)
    private String updateTimeStr;
}