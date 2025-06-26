
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
@TableName("sys_timed_task_log")
public class TimedTaskLog extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 任务日志ID */

    @TableId(value = "task_log_id", type = IdType.AUTO)
    private Long taskbLogId;
    /** 任务名称 */
    @TableField(value = "task_name")
    private String taskName;
    /** 任务组名 */
    @TableField(value = "task_group")
    private String taskGroup;
    /** 调用目标字符串 */
    @TableField(value = "invoke_target")
    private String invokeTarget;
    /** 日志信息 */
    @TableField(value = "task_message")
    private String taskMessage;
    /** 执行状态（0正常 1失败） */
    @TableField(value = "status")
    private Byte status;
    /** 异常信息 */
    @TableField(value = "exception_info")
    private String exceptionInfo;

    /** 开始时间 */
    private Long startTime;

    /** 停止时间 */
    private Long stopTime;

}