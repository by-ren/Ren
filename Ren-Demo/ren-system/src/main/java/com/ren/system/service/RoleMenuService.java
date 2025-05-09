package com.ren.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ren.system.entity.RoleMenu;
import com.ren.system.entity.RoleMenu;

import java.util.List;

public interface RoleMenuService extends IService<RoleMenu> {

    /*
     * 添加角色菜单
     * @param roleMenu
     * @author admin
     * @date 2025/05/07 17:17
     */
    void addRoleMenu(RoleMenu roleMenu);

    /*
     * 删除角色菜单
     * @param roleId
     * @author admin
     * @date 2025/05/07 17:18
     */
    void removeRoleMenuByRoleId(long roleId);

    /*
     * 获取角色菜单列表
     * @param roleId
     * @return java.util.List<com.ren.system.entity.RoleMenu>
     * @author admin
     * @date 2025/05/07 17:19
     */
    List<RoleMenu> listRoleMenuByRoleId(long roleId);

}