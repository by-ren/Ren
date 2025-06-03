
package com.ren.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ren.common.domain.entity.Role;
import com.ren.common.domain.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface RoleMapper extends BaseMapper<Role> {

    /*
     * 添加角色
     * @param createBy
     * @return long
     * @author ren
     * @date 2025/05/07 17:12
     */
    void insertRole(Role role);

    /*
     * 编辑角色是否删除
     * @param roleId
     * @param isDel
     * @param updateBy
     * @author ren
     * @date 2025/05/07 17:13
     */
    void updateRoleIsDelById(@Param("roleId")long roleId, @Param("isDel")byte isDel, @Param("updateBy")String updateBy, @Param("updateTime")long updateTime);

    /*
     * 编辑角色
     * @param role
     * @param updateBy
     * @author ren
     * @date 2025/05/07 17:13
     */
    void updateRoleById(Role role);

    /*
     * 根据参数获取角色列表
     * @param paramMap
     * @return java.util.List<com.ren.common.core.entity.Role>
     * @author ren
     * @date 2025/05/07 17:15
     */
    IPage<Role> listRoleByPage(Page<Role> page, @Param("paramMap")Map<String, Object> paramMap);

    /*
     * 根据参数获取角色列表
     * @param paramMap
     * @return java.util.List<com.ren.common.domain.entity.Role>
     * @author ren
     * @date 2025/05/13 20:05
     */
    List<Role> listRoleByParam(Map<String, Object> paramMap);

    /*
     * 根据用户Id获取角色列表
     * @param userId
     * @return java.util.List<com.ren.common.domain.entity.Role>
     * @author ren
     * @date 2025/05/16 14:37
     */
    List<Role> listRoleByUserId(Long userId);
}