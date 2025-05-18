package com.ren.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ren.common.domain.base.BaseEntity;
import com.ren.common.domain.entity.Dept;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_logininfor")
public class Logininfor{
    private static final long serialVersionUID = 1L;

    @TableId(value = "info_id", type = IdType.AUTO)
    private Long infoId;
    /**用户账号*/
    @TableField(value = "user_name")
    private String userName;
    /**登录IP地址*/
    @TableField(value = "ipaddr")
    private String ipaddr;
    /**登录地点*/
    @TableField(value = "login_location")
    private String loginLocation;
    /**浏览器类型*/
    @TableField(value = "browser")
    private String browser;
    /**操作系统*/
    @TableField(value = "os")
    private String os;
    /**是否登陆成功（1：是 0：否）*/
    @TableField(value = "is_success")
    private Byte isSuccess;
    /**提示消息*/
    @TableField(value = "msg")
    private String msg;
    /**访问时间*/
    @TableField(value = "login_time")
    private Long loginTime;

    /*==================================================以下为冗余字段===================================================*/
    /**访问时间字符串*/
    @TableField(exist = false)
    private String loginTimeStr;

}