package com.ren.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ren.common.core.domain.entity.Role;

import java.util.List;
import java.util.Map;

public interface RoleService extends IService<Role> {

    /**
     * 添加角色
     * @param createBy
     * @return long
     * @author ren
     * @date 2025/05/07 17:12
     */
    long addRole(Role role, String createBy);

    /**
     * 编辑角色是否删除
     * @param roleId
     * @param isDel
     * @param updateBy
     * @author ren
     * @date 2025/05/07 17:13
     */
    void modifyRoleIsDelById(long roleId,byte isDel,String updateBy);

    /**
     * 编辑角色
     * @param role
     * @param updateBy
     * @author ren
     * @date 2025/05/07 17:13
     */
    void modifyRoleById(Role role,String updateBy);

    /**
     * 获取角色详情
     * @param roleId
     * @return com.ren.common.core.entity.Role
     * @author ren
     * @date 2025/05/07 17:14
     */
    Role getRoleById(long roleId);

    /**
     * 根据参数获取角色列表
     * @param paramMap
     * @return java.util.List<com.ren.common.core.entity.Role>
     * @author ren
     * @date 2025/05/07 17:15
     */
    IPage<Role> listRoleByPage(Map<String,Object> paramMap);

    /**
     * 根据参数获取角色列表
     * @param paramMap
     * @return java.util.List<com.ren.common.domain.entity.Role>
     * @author ren
     * @date 2025/05/13 20:05
     */
    List<Role> listRoleByParam(Map<String,Object> paramMap);

    /**
     * 根据用户Id获取角色列表
     * @param userId
     * @return java.util.List<com.ren.common.domain.entity.Role>
     * @author ren
     * @date 2025/05/16 14:36
     */
    List<Role> listRoleByUserId(Long userId);
}