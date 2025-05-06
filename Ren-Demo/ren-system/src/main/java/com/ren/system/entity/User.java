package com.ren.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ren.common.base.BaseEntity;
import lombok.*;
import org.apache.ibatis.type.Alias;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_user")
public class User extends BaseEntity implements UserDetails{

    private static final long serialVersionUID = 1L;

    //对应数据库中的名称为userId，并且是主键自增
    @TableId(value = "user_id", type = IdType.AUTO)
    private Long userId;
    /**用户账号*/
    private String username;
    /**用户昵称*/
    private String nickname;
    /**登陆密码*/
    private String password;
    /**是否删除*/
    @TableField(value = "is_del")
    private Byte isDel;
    /**部门ID*/
    @TableField(value = "dept_id")
    private Long deptId;
    /**用户类型（00系统用户）*/
    @TableField(value = "user_type")
    private String userType;
    /**用户邮箱*/
    @TableField(value = "email")
    private String email;
    /**手机号码*/
    @TableField(value = "phonenumber")
    private String phonenumber;
    /**性别*/
    @TableField(value = "sex")
    private Byte sex;
    /**头像地址*/
    @TableField(value = "avatar")
    private String avatar;
    /**最后登录IP*/
    @TableField(value = "login_ip")
    private String loginIp;
    /**最后登录时间 (秒时间戳)*/
    @TableField(value = "login_date")
    private Long loginDate;

    /*==================================================以下为冗余字段===================================================*/
    /**权限列表*/
    @TableField(exist = false)
    private List<String> roles = new ArrayList<>();

    /*====================================以下为SpringSecurity中UserDetail所需字段=======================================*/

    /**是否启用*/
    @Getter(AccessLevel.NONE)
    @TableField(exist = false)
    private Boolean enabled;
    /**账号是否未过期*/
    @Getter(AccessLevel.NONE)
    @TableField(exist = false)
    private Boolean accountNonExpired;
    /**账号是否未锁定*/
    @Getter(AccessLevel.NONE)
    @TableField(exist = false)
    private Boolean accountNonLocked;
    /**证书是否未过期*/
    @Getter(AccessLevel.NONE)
    @TableField(exist = false)
    private Boolean credentialsNonExpired;

    /*===================================以下为SpringSecurity中UserDetail所调用方法=======================================*/

    /*
     * 将数据库中查询出来的角色转换为SpringSecurity可以认识的权限对象，SpringSecurity会自己调用这个方法，来获取权限
     * @return java.util.Collection<? extends org.springframework.security.core.GrantedAuthority>
     * @author admin
     * @date 2025/04/29 21:01
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new) // 将字符串角色转换为权限对象
                .collect(Collectors.toList());
    }

    /*
     * 将数据库查询出来的是否启用进行返回，SpringSecurity会自己调用这个方法，来判断账号是否启用，默认为启用
     * @return boolean
     * @author admin
     * @date 2025/04/29 21:01
     */
    @Override
    public boolean isEnabled() {
        return enabled != null ? enabled : true;
    }

    /*
     * 将数据库查询出来的用户账号是否未过期进行返回，SpringSecurity会自己调用这个方法，来判断账号是否启用，默认为未过期
     * @return boolean
     * @author admin
     * @date 2025/04/29 21:01
     */
    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired != null ? accountNonExpired : true;
    }

    /*
     * 将数据库查询出来的用户账户是否未锁定进行返回，SpringSecurity会自己调用这个方法，来判断账号是否锁定，默认为未锁定
     * @return boolean
     * @author admin
     * @date 2025/04/29 21:02
     */
    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked != null ? accountNonLocked : true;
    }

    /*
     * 将数据库查询出来的用户证书是否未过期进行返回，SpringSecurity会自己调用这个方法，来判断账号是否启用，默认为未锁定
     * @return boolean
     * @author admin
     * @date 2025/04/29 21:02
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired != null ? credentialsNonExpired : true;
    }

}