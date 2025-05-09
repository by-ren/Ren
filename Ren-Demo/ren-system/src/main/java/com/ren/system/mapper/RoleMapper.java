
package com.ren.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ren.common.core.entity.Role;
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
     * @author admin
     * @date 2025/05/07 17:12
     */
    void insertRole(Role role);

    /*
     * 编辑角色是否删除
     * @param roleId
     * @param isDel
     * @param updateBy
     * @author admin
     * @date 2025/05/07 17:13
     */
    void updateRoleIsDelById(@Param("roleId")long roleId, @Param("isDel")byte isDel, @Param("updateBy")String updateBy, @Param("updateTime")long updateTime);

    /*
     * 编辑角色
     * @param role
     * @param updateBy
     * @author admin
     * @date 2025/05/07 17:13
     */
    void updateRoleById(Role role);

    /*
     * 根据参数获取角色列表
     * @param paramMap
     * @return java.util.List<com.ren.common.core.entity.Role>
     * @author admin
     * @date 2025/05/07 17:15
     */
    List<Role> listRoleByParam(Map<String, Object> paramMap);
}