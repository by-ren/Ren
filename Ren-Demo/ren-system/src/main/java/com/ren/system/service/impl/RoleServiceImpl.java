
package com.ren.system.service.impl;

import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ren.common.core.constant.AppConstants;
import com.ren.common.core.domain.entity.Role;
import com.ren.common.utils.DateUtils;
import com.ren.common.utils.PageUtils;
import com.ren.common.utils.StringUtils;
import com.ren.system.entity.RoleDept;
import com.ren.system.entity.RoleMenu;
import com.ren.system.mapper.RoleMapper;
import com.ren.system.service.DeptService;
import com.ren.system.service.RoleDeptService;
import com.ren.system.service.RoleMenuService;
import com.ren.system.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private RoleMenuService roleMenuService;
    @Autowired
    private RoleDeptService roleDeptService;
    @Autowired
    private DeptService deptService;

    /**
     * 添加角色
     * @param createBy
     * @return long
     * @author ren
     * @date 2025/05/07 17:12
     */
    @Override
    @Transactional(readOnly = false)
    public long addRole(Role role, String createBy) {
        role.setIsDel(AppConstants.COMMON_BYTE_NO);
        role.setCreateBy(createBy);
        role.setCreateTime(DateUtils.currentSeconds());
        roleMapper.insertRole(role);
        if(role.getMenuIds() != null && role.getMenuIds().length > 0){
            //添加权限菜单
            List<RoleMenu> roleMenuList = Arrays.stream(role.getMenuIds()).map(menuId -> new RoleMenu(role.getRoleId(),menuId)).collect(Collectors.toList());
            roleMenuService.addRoleMenuBatch(roleMenuList);
        }
        if(role.getDataScope() == AppConstants.DATA_SCOPE_CUSTOMIZE) {
            if (role.getDeptIds() != null && role.getDeptIds().length > 0) {
                //添加权限部门
                List<RoleDept> roleDeptList = Arrays.stream(role.getDeptIds()).map(deptId -> new RoleDept(role.getRoleId(), deptId)).collect(Collectors.toList());
                roleDeptService.addRoleDeptBatch(roleDeptList);
            }
        }
        return role.getRoleId();
    }

    /**
     * 编辑角色是否删除
     * @param roleId
     * @param isDel
     * @param updateBy
     * @author ren
     * @date 2025/05/07 17:13
     */
    @Override
    public void modifyRoleIsDelById(long roleId, byte isDel, String updateBy) {
        roleMapper.updateRoleIsDelById(roleId,isDel,updateBy,DateUtils.currentSeconds());
    }

    /**
     * 编辑角色
     * @param role
     * @param updateBy
     * @author ren
     * @date 2025/05/07 17:13
     */
    @Override
    public void modifyRoleById(Role role, String updateBy) {
        role.setUpdateBy(updateBy);
        role.setUpdateTime(DateUtils.currentSeconds());
        roleMapper.updateRoleById(role);
        //删除角色权限菜单
        roleMenuService.removeRoleMenuByRoleId(role.getRoleId());
        if(role.getMenuIds() != null && role.getMenuIds().length > 0){
            //重新添加权限菜单
            List<RoleMenu> roleMenuList = Arrays.stream(role.getMenuIds()).map(menuId -> new RoleMenu(role.getRoleId(),menuId)).collect(Collectors.toList());
            roleMenuService.addRoleMenuBatch(roleMenuList);
        }
        if(role.getDataScope() == AppConstants.DATA_SCOPE_CUSTOMIZE){
            //删除角色权限部门
            roleDeptService.removeRoleDeptByRoleId(role.getRoleId());
            if(role.getDeptIds() != null && role.getDeptIds().length > 0){
                //添加权限部门
                List<RoleDept> roleDeptList = Arrays.stream(role.getDeptIds()).map(deptId -> new RoleDept(role.getRoleId(),deptId)).collect(Collectors.toList());
                roleDeptService.addRoleDeptBatch(roleDeptList);
            }
        }
    }

    /**
     * 获取角色详情
     * @param roleId
     * @return com.ren.common.core.entity.Role
     * @author ren
     * @date 2025/05/07 17:14
     */
    @Override
    public Role getRoleById(long roleId) {
        Role role = roleMapper.selectById(roleId);
        return role;
    }

    /**
     * 根据参数获取角色列表
     * @param paramMap
     * @return java.util.List<com.ren.common.core.entity.Role>
     * @author ren
     * @date 2025/05/07 17:15
     */
    @Override
    public IPage<Role> listRoleByPage(Map<String, Object> paramMap) {
        if(paramMap != null && paramMap.containsKey("searchLike") && StringUtils.isNotBlank(Convert.toStr(paramMap.get("searchLike")))){
            paramMap.put("searchLike", StringUtils.format("%%{}%%",paramMap.get("searchLike")));
        }
        IPage<Role> roleList = roleMapper.listRoleByPage(PageUtils.createPage(Role.class),paramMap);
        return roleList;
    }

    /**
     * 根据参数获取角色列表
     * @param paramMap
     * @return java.util.List<com.ren.common.domain.entity.Role>
     * @author ren
     * @date 2025/05/13 20:05
     */
    @Override
    public List<Role> listRoleByParam(Map<String, Object> paramMap) {
        if(paramMap != null && paramMap.containsKey("searchLike") && StringUtils.isNotBlank(Convert.toStr(paramMap.get("searchLike")))){
            paramMap.put("searchLike", StringUtils.format("%%{}%%",paramMap.get("searchLike")));
        }
        return roleMapper.listRoleByParam(paramMap);
    }

    /**
     * 根据用户Id获取角色列表
     * @param userId
     * @return java.util.List<com.ren.common.domain.entity.Role>
     * @author ren
     * @date 2025/05/16 14:36
     */
    @Override
    public List<Role> listRoleByUserId(Long userId) {
        return roleMapper.listRoleByUserId(userId);
    }
}