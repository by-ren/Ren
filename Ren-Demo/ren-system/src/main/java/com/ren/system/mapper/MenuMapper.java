package com.ren.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ren.common.core.entity.Menu;
import org.apache.ibatis.annotations.Mapper;

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
}