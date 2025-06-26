
package com.ren.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ren.system.entity.RoleMenu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RoleMenuMapper extends BaseMapper<RoleMenu> {

    /**
     * 添加角色菜单
     * @param roleMenu
     * @author ren
     * @date 2025/05/07 17:17
     */
    void insertRoleMenu(RoleMenu roleMenu);

    /**
     * 删除角色菜单
     * @param roleId
     * @author ren
     * @date 2025/05/07 17:18
     */
    void deleteRoleMenuByRoleId(long roleId);

    /**
     * 获取角色菜单列表
     * @param roleId
     * @return java.util.List<com.ren.system.entity.RoleMenu>
     * @author ren
     * @date 2025/05/07 17:19
     */
    List<RoleMenu> listRoleMenuByRoleId(long roleId);

    /**
     * 添加权限菜单
     * @param roleMenuList
     * @author ren
     * @date 2025/05/12 15:45
     */
	void insertRoleMenuBatch(List<RoleMenu> roleMenuList);
}