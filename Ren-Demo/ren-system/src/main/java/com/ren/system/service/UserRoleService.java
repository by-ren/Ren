package com.ren.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ren.system.entity.UserRole;

import java.util.List;

public interface UserRoleService extends IService<UserRole> {

    /**
     * 添加用户角色
     * @param userRole
     * @author ren
     * @date 2025/05/07 17:17
     */
    void addUserRole(UserRole userRole);

    /**
     * 批量添加用户角色
     * @param userRoleList
     * @author ren
     * @date 2025/05/23 14:45
     */
    void addUserRoleBatch(List<UserRole> userRoleList);

    /**
     * 删除用户角色
     * @param userId
     * @author ren
     * @date 2025/05/07 17:18
     */
    void removeUserRoleByUserId(long userId);

    /**
     * 获取用户角色列表
     * @param userId
     * @return java.util.List<com.ren.system.entity.UserRole>
     * @author ren
     * @date 2025/05/07 17:19
     */
    List<UserRole> listUserRoleByUserId(long userId);

}