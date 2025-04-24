package com.ren.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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
@TableName("user")
public class User implements UserDetails {
    //对应数据库中的名称为id，并且是主键自增
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;        // 使用 Long 类型对应前端 number
    private String username;
    private String password;
    private Boolean enabled;
    private Boolean accountNonExpired;
    private Boolean accountNonLocked;
    private Boolean credentialsNonExpired;

    @TableField(exist = false)
    private List<String> roles = new ArrayList<>();

    // 将数据库中查询出来的角色转换为SpringSecurity可以认识的权限对象，SpringSecurity会自己调用这个方法，来获取权限
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new) // 将字符串角色转换为权限对象
                .collect(Collectors.toList());
    }

    // 将数据库查询出来的是否启用进行返回，SpringSecurity会自己调用这个方法，来判断账号是否启用，默认为启用
    @Override
    public boolean isEnabled() {
        return enabled != null ? enabled : true;
    }

    // 将数据库查询出来的用户账号是否未过期进行返回，SpringSecurity会自己调用这个方法，来判断账号是否启用，默认为未过期
    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired != null ? accountNonExpired : true;
    }

    // 将数据库查询出来的用户凭证是否未过期进行返回，SpringSecurity会自己调用这个方法，来判断账号是否启用，默认为未过期
    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked != null ? accountNonLocked : true;
    }

    // 将数据库查询出来的用户是否未被锁定进行返回，SpringSecurity会自己调用这个方法，来判断账号是否启用，默认为未锁定
    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired != null ? credentialsNonExpired : true;
    }

}