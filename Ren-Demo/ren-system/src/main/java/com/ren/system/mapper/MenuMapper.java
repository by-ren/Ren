package com.ren.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ren.common.domain.entity.Menu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface MenuMapper extends BaseMapper<Menu> {

    /*
     * 添加菜单
     * @param createBy
     * @return long
     * @author admin
     * @date 2025/05/07 17:12
     */
    void insertMenu(Menu menu);

    /*
     * 编辑菜单
     * @param menu
     * @param updateBy
     * @author admin
     * @date 2025/05/07 17:13
     */
    void updateMenuById(Menu menu);

    /*
     * 根据参数获取菜单列表
     * @param paramMap
     * @return java.util.List<com.ren.common.core.entity.Menu>
     * @author admin
     * @date 2025/05/07 17:15
     */
    List<Menu> listMenuByParam(Map<String, Object> paramMap);

    /*
     * 编辑菜单是否删除
     * @param deptId
     * @param isDel
     * @param updateBy
     * @author admin
     * @date 2025/05/07 17:13
     */
	void updateMenuIsDelById(@Param("menuId") long menuId, @Param("isDel")byte isDel, @Param("updateBy")String updateBy, @Param("updateTime")long updateTime);

    /*
     * 根据角色ID获取菜单列表
     * @param roleIds
     * @return java.util.List<com.ren.common.domain.entity.Menu>
     * @author admin
     * @date 2025/05/16 14:49
     */
    List<Menu> listMenuByRoleIds(@Param("roleIds") Long[] roleIds);
}