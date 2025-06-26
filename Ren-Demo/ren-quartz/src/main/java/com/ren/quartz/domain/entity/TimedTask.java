package com.ren.quartz.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ren.common.domain.entity.BaseEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_timed_task")
public class TimedTask extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 任务ID */
    @TableId(value = "task_id", type = IdType.AUTO)
    private Long taskId;
    /** 任务名称 */
    @TableField(value = "task_name")
    private String taskName;
    /** 任务组名 */
    @TableField(value = "task_group")
    private String taskGroup;
    /** 调用目标字符串 */
    @TableField(value = "invoke_target")
    private String invokeTarget;
    /** cron执行表达式 */
    @TableField(value = "cron_expression")
    private String cronExpression;
    /** 计划执行错误策略 参数取值见QuartzContents */
    @TableField(value = "misfire_policy")
    private Byte misfirePolicy;
    /** 是否并发执行 参数取值见AppConstants */
    @TableField(value = "concurrent")
    private Byte concurrent;
    /** 状态 参数取值见QuartzContents */
    @TableField(value = "status")
    private Byte status;

}