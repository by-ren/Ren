
package com.ren.system.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ren.common.domain.entity.Menu;
import com.ren.system.mapper.MenuMapper;
import com.ren.system.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Autowired
    private  MenuMapper menuMapper;

    /*
     * 添加菜单
     * @param createBy
     * @return long
     * @author admin
     * @date 2025/05/07 17:12
     */
    @Override
    public long addMenu(Menu menu, String createBy) {
        menu.setCreateBy(createBy);
        menu.setCreateTime(DateUtil.currentSeconds());
        menuMapper.insertMenu(menu);
        return menu.getMenuId();
    }

    /*
     * 编辑菜单
     * @param menu
     * @param updateBy
     * @author admin
     * @date 2025/05/07 17:13
     */
    @Override
    public void modifyMenuById(Menu menu, String updateBy) {
        menu.setUpdateBy(updateBy);
        menu.setUpdateTime(DateUtil.currentSeconds());
        menuMapper.updateMenuById(menu);
    }

    /*
     * 获取菜单详情
     * @param menuId
     * @return com.ren.common.core.entity.Menu
     * @author admin
     * @date 2025/05/07 17:14
     */
    @Override
    public Menu getMenuById(long menuId) {
        Menu menu = menuMapper.selectById(menuId);
        return menu;
    }

    /*
     * 根据参数获取菜单列表
     * @param paramMap
     * @return java.util.List<com.ren.common.core.entity.Menu>
     * @author admin
     * @date 2025/05/07 17:15
     */
    @Override
    public List<Menu> listMenuByParam(Map<String, Object> paramMap) {
        if(paramMap != null && paramMap.containsKey("searchLike") && StrUtil.isNotBlank(Convert.toStr(paramMap.get("searchLike")))){
            paramMap.put("searchLike", StrUtil.format("%%{}%%",paramMap.get("searchLike")));
        }
        List<Menu> menuList = menuMapper.listMenuByParam(paramMap);
        return menuList;
    }

    /*
     * 编辑菜单是否删除
     * @param deptId
     * @param isDel
     * @param updateBy
     * @author admin
     * @date 2025/05/07 17:13
     */
    @Override
    public void modifyMenuIsDelById(long menuId, byte isDel, String updateBy) {
        menuMapper.updateMenuIsDelById(menuId,isDel,updateBy,DateUtil.currentSeconds());
    }

    /*
     * 根据角色ID获取菜单列表
     * @param roleId
     * @return java.util.List<com.ren.common.core.entity.Menu>
     * @author admin
     * @date 2025/05/07 17:16
     */
    @Override
    public List<Menu> listMenuByRoleIds(Long... roleIds) {
        List<Menu> menuList = menuMapper.listMenuByRoleIds(roleIds);
        return menuList;
    }
}