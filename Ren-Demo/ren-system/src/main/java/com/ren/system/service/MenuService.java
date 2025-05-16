package com.ren.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ren.common.domain.entity.Menu;

import java.util.List;
import java.util.Map;

public interface MenuService extends IService<Menu> {

    /*
     * 添加菜单
     * @param createBy
     * @return long
     * @author admin
     * @date 2025/05/07 17:12
     */
    long addMenu(Menu menu, String createBy);

    /*
     * 编辑菜单
     * @param menu
     * @param updateBy
     * @author admin
     * @date 2025/05/07 17:13
     */
    void modifyMenuById(Menu menu,String updateBy);

    /*
     * 获取菜单详情
     * @param menuId
     * @return com.ren.common.core.entity.Menu
     * @author admin
     * @date 2025/05/07 17:14
     */
    Menu getMenuById(long menuId);

    /*
     * 根据参数获取菜单列表
     * @param paramMap
     * @return java.util.List<com.ren.common.core.entity.Menu>
     * @author admin
     * @date 2025/05/07 17:15
     */
    List<Menu> listMenuByParam(Map<String,Object> paramMap);

    /*
     * 编辑菜单是否删除
     * @param deptId
     * @param isDel
     * @param updateBy
     * @author admin
     * @date 2025/05/07 17:13
     */
    void modifyMenuIsDelById(long menuId,byte isDel,String updateBy);

    /*
     * 根据橘色标识获取菜单列表
     * @param roleId
     * @return java.util.List<com.ren.common.domain.entity.Menu>
     * @author admin
     * @date 2025/05/16 14:48
     */
	List<Menu> listMenuByRoleIds(Long... roleIds);
}