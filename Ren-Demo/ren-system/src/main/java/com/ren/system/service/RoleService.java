package com.ren.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ren.common.core.entity.Role;

import java.util.List;
import java.util.Map;

public interface RoleService extends IService<Role> {

    /*
     * 添加角色
     * @param createBy
     * @return long
     * @author admin
     * @date 2025/05/07 17:12
     */
    long addRole(Role role, String createBy);

    /*
     * 编辑角色是否删除
     * @param roleId
     * @param isDel
     * @param updateBy
     * @author admin
     * @date 2025/05/07 17:13
     */
    void modifyRoleIsDelById(long roleId,byte isDel,String updateBy);

    /*
     * 编辑角色
     * @param role
     * @param updateBy
     * @author admin
     * @date 2025/05/07 17:13
     */
    void modifyRoleById(Role role,String updateBy);

    /*
     * 获取角色详情
     * @param roleId
     * @return com.ren.common.core.entity.Role
     * @author admin
     * @date 2025/05/07 17:14
     */
    Role getRoleById(long roleId);

    /*
     * 根据参数获取角色列表
     * @param paramMap
     * @return java.util.List<com.ren.common.core.entity.Role>
     * @author admin
     * @date 2025/05/07 17:15
     */
    List<Role> listRoleByParam(Map<String,Object> paramMap);

}