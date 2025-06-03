
package com.ren.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ren.system.entity.RoleMenu;
import com.ren.system.mapper.RoleMenuMapper;
import com.ren.system.service.RoleMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu> implements RoleMenuService {

    @Autowired
    private RoleMenuMapper roleMenuMapper;

    /**
     * 添加角色菜单
     * @param roleMenu
     * @author ren
     * @date 2025/05/07 17:17
     */
    @Override
    public void addRoleMenu(RoleMenu roleMenu) {
        roleMenuMapper.insertRoleMenu(roleMenu);
    }

    /**
     * 添加权限菜单
     * @param roleMenuList
     * @author ren
     * @date 2025/05/12 15:44
     */
    @Override
    public void addRoleMenuBatch(List<RoleMenu> roleMenuList) {
        roleMenuMapper.insertRoleMenuBatch(roleMenuList);
    }

    /**
     * 删除角色菜单
     * @param roleId
     * @author ren
     * @date 2025/05/07 17:18
     */
    @Override
    public void removeRoleMenuByRoleId(long roleId) {
        roleMenuMapper.deleteRoleMenuByRoleId(roleId);
    }

    /**
     * 获取角色菜单列表
     * @param roleId
     * @return java.util.List<com.ren.system.entity.RoleMenu>
     * @author ren
     * @date 2025/05/07 17:19
     */
    @Override
    public List<RoleMenu> listRoleMenuByRoleId(long roleId) {
        List<RoleMenu> roleMenuList = roleMenuMapper.listRoleMenuByRoleId(roleId);
        return roleMenuList;
    }

}