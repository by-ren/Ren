package com.ren.common.core.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_user")
public class User extends BaseEntity{

    private static final long serialVersionUID = 1L;

    /** 对应数据库中的名称为userId，并且是主键自增 */
    @TableId(value = "user_id", type = IdType.AUTO)
    private Long userId;
    /** 用户账号 */
    private String username;
    /** 用户昵称 */
    private String nickname;
    /** 登陆密码 */
    private String password;
    /** 是否删除 */
    @TableField(value = "is_del")
    private Byte isDel;
    /** 部门ID */
    @TableField(value = "dept_id")
    private Long deptId;
    /** 用户类型（00系统用户） */
    @TableField(value = "user_type")
    private String userType;
    /** 用户邮箱 */
    @TableField(value = "email")
    private String email;
    /** 手机号码 */
    @TableField(value = "phonenumber")
    private String phonenumber;
    /** 性别 */
    @TableField(value = "sex")
    private Byte sex;
    /** 头像地址 */
    @TableField(value = "avatar")
    private String avatar;
    /** 最后登录IP */
    @TableField(value = "login_ip")
    private String loginIp;
    /** 最后登录时间 (秒时间戳) */
    @TableField(value = "login_date")
    private Long loginDate;

    /*==================================================以下为冗余字段===================================================*/
    /** 部门 */
    @TableField(exist = false)
    private Dept dept;
    /** 角色列表 */
    @TableField(exist = false)
    private List<Role> roleList = new ArrayList<>();
    /** 角色Id */
    @TableField(exist = false)
    private Long[] roleIdArr;
    /** 岗位Id */
    @TableField(exist = false)
    private Long[] postIdArr;
    /** 最后登录时间Str */
    @TableField(exist = false)
    private String loginDateStr;

}