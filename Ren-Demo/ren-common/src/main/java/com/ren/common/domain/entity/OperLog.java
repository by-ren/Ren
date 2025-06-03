package com.ren.common.domain.entity;

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
@TableName("sys_oper_log")
public class OperLog {
    private static final long serialVersionUID = 1L;

    /** 日志主键 */
    @TableId(value = "oper_id", type = IdType.AUTO)
    private Long operId;
    /** 模块标题 */
    @TableField(value = "title")
    private String title;
    /** 业务类型（0其它 1新增 2修改 3删除） */
    @TableField(value = "business_type")
    private Byte businessType;
    /** 方法名称 */
    @TableField(value = "method")
    private String method;
    /** 请求方式 */
    @TableField(value = "request_method")
    private String requestMethod;
    /** 操作类别（0其它 1后台用户 2手机端用户） */
    @TableField(value = "operator_type")
    private Byte operatorType;
    /** 操作人员 */
    @TableField(value = "oper_name")
    private String operName;
    /** 部门名称 */
    @TableField(value = "dept_name")
    private String deptName;
    /** 请求URL */
    @TableField(value = "oper_url")
    private String operUrl;
    /** 主机地址 */
    @TableField(value = "oper_ip")
    private String operIp;
    /** 操作地点 */
    @TableField(value = "oper_location")
    private String operLocation;
    /** 请求参数 */
    @TableField(value = "oper_param")
    private String operParam;
    /** 返回参数 */
    @TableField(value = "json_result")
    private String jsonResult;
    /** 是否正常（1：是 0：否） */
    @TableField(value = "is_normal")
    private Byte isNormal;
    /** 错误消息 */
    @TableField(value = "error_msg")
    private String errorMsg;
    /** 操作时间（时间戳：秒） */
    @TableField(value = "oper_time")
    private Long operTime;
    /** 消耗时间 */
    @TableField(value = "cost_time")
    private Long costTime;


    /*==================================================以下为冗余字段===================================================*/
    /** 操作时间字符串 */
    @TableField(exist = false)
    private String operTimeStr;

}