
package com.ren.system.service.impl;

import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ren.common.domain.entity.Menu;
import com.ren.common.utils.DateUtils;
import com.ren.common.utils.StringUtils;
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

    /**
     * 添加菜单
     * @param createBy
     * @return long
     * @author ren
     * @date 2025/05/07 17:12
     */
    @Override
    public long addMenu(Menu menu, String createBy) {
        menu.setCreateBy(createBy);
        menu.setCreateTime(DateUtils.currentSeconds());
        menuMapper.insertMenu(menu);
        return menu.getMenuId();
    }

    /**
     * 编辑菜单
     * @param menu
     * @param updateBy
     * @author ren
     * @date 2025/05/07 17:13
     */
    @Override
    public void modifyMenuById(Menu menu, String updateBy) {
        menu.setUpdateBy(updateBy);
        menu.setUpdateTime(DateUtils.currentSeconds());
        menuMapper.updateMenuById(menu);
    }

    /**
     * 获取菜单详情
     * @param menuId
     * @return com.ren.common.core.entity.Menu
     * @author ren
     * @date 2025/05/07 17:14
     */
    @Override
    public Menu getMenuById(long menuId) {
        Menu menu = menuMapper.selectById(menuId);
        return menu;
    }

    /**
     * 根据参数获取菜单列表
     * @param paramMap
     * @return java.util.List<com.ren.common.core.entity.Menu>
     * @author ren
     * @date 2025/05/07 17:15
     */
    @Override
    public List<Menu> listMenuByParam(Map<String, Object> paramMap) {
        if(paramMap != null && paramMap.containsKey("searchLike") && StringUtils.isNotBlank(Convert.toStr(paramMap.get("searchLike")))){
            paramMap.put("searchLike", StringUtils.format("%%{}%%",paramMap.get("searchLike")));
        }
        List<Menu> menuList = menuMapper.listMenuByParam(paramMap);
        return menuList;
    }

    /**
     * 编辑菜单是否删除
     * @param menuId
     * @param isDel
     * @param updateBy
     * @author ren
     * @date 2025/05/07 17:13
     */
    @Override
    public void modifyMenuIsDelById(long menuId, byte isDel, String updateBy) {
        menuMapper.updateMenuIsDelById(menuId,isDel,updateBy,DateUtils.currentSeconds());
    }

    /**
     * 根据角色ID获取菜单列表
     * @param roleIds
     * @return java.util.List<com.ren.common.core.entity.Menu>
     * @author ren
     * @date 2025/05/07 17:16
     */
    @Override
    public List<Menu> listMenuByRoleIds(Long... roleIds) {
        List<Menu> menuList = menuMapper.listMenuByRoleIds(roleIds);
        return menuList;
    }
}