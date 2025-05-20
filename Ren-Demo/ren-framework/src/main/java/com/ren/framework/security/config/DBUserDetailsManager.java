package com.ren.framework.security.config;

import com.ren.common.domain.bo.LoginUser;
import com.ren.common.domain.entity.Role;
import com.ren.common.domain.entity.User;
import com.ren.system.mapper.RoleMapper;
import com.ren.system.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @description 本类是基于数据库的用户信息管理器
 * @author Ren
 * @date 2025/4/16
 */
@Component
public class DBUserDetailsManager implements UserDetailsManager, UserDetailsPasswordService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoleMapper roleMapper;

    /*
     * Spring Security的核心方法：根据用户名加载用户
     * Spring Security在进行认证的时候会自动调用，然后用这里的内容与用户传进来的内容对比，如果相同，则认证成功
     * @param username
     * @return org.springframework.security.core.userdetails.UserDetails
     * @author admin
     * @date 2025/04/17 21:31
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("username",username);
        User user = userMapper.selectUserByParam(paramMap);

        // 返回的User对象必须实现UserDetails接口
        // 此处完成数据库用户到安全上下文的转换
        if (user == null) {
            // 用户不存在
            throw new UsernameNotFoundException(username);
        } else {
            List<Role> roleList = roleMapper.listRoleByUserId(user.getUserId());
            user.setRoleList(roleList);
			return new LoginUser(user.getUserId(), user, roleList.stream().map(Role::getRoleKey).collect(Collectors.toSet()));
        }
    }

    @Override
    public UserDetails updatePassword(UserDetails userDetails, String newPassword) {
        return null;
    }

    @Override
    public void createUser(UserDetails userDetails) {
    }

    @Override
    public void updateUser(UserDetails userDetails) {

    }

    @Override
    public void deleteUser(String username) {

    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {

    }

    @Override
    public boolean userExists(String username) {
        return false;
    }
}