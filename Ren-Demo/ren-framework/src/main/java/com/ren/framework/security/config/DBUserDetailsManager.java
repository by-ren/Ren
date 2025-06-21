package com.ren.framework.security.config;

import cn.hutool.core.util.StrUtil;
import com.ren.common.constant.AppConstants;
import com.ren.common.domain.model.bo.LoginUser;
import com.ren.common.domain.entity.Menu;
import com.ren.common.domain.entity.Role;
import com.ren.common.domain.entity.User;
import com.ren.system.mapper.MenuMapper;
import com.ren.system.mapper.RoleMapper;
import com.ren.system.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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
    private MenuMapper menuMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoleMapper roleMapper;

    /*
     * Spring Security的核心方法：根据用户名加载用户
     * Spring Security在进行认证的时候会自动调用，然后用这里的内容与用户传进来的内容对比，如果相同，则认证成功
     * @param username
     * @return org.springframework.security.core.userdetails.UserDetails
     * @author ren
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
            Long[] roleIds = roleList.stream().map(Role::getRoleId).toArray(Long[]::new);
            //是否Admin
            boolean isAdmin = roleList.stream().anyMatch(role -> role.getRoleKey().equals(AppConstants.ROLE_SUPER_KEY));
            //获取用所拥有权限，并返回（前端用于判断按钮权限）
            List<Menu> menuList = new ArrayList<>();
            if(isAdmin){
                //超级管理员获取所有菜单
                menuList = menuMapper.listMenuByParam(null);
            }else if(roleIds.length > 0){
                //不是超级管理员获取相应菜单
                menuList = menuMapper.listMenuByRoleIds(roleIds);
            }
			return new LoginUser(user.getUserId(), user, menuList.stream().filter(menu -> !menu.getMenuType().equals(AppConstants.MENU_TYPE_DIR) && StrUtil.isNotBlank(menu.getPerms())).map(Menu::getPerms).collect(Collectors.toSet()));
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