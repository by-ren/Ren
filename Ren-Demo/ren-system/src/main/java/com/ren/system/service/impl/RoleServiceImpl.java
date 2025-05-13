
package com.ren.system.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ren.common.constant.AppConstants;
import com.ren.common.domain.entity.Role;
import com.ren.common.domain.entity.User;
import com.ren.common.utils.PageUtils;
import com.ren.system.entity.RoleDept;
import com.ren.system.entity.RoleMenu;
import com.ren.system.mapper.RoleMapper;
import com.ren.system.service.*;
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

    /*
     * 添加角色
     * @param createBy
     * @return long
     * @author admin
     * @date 2025/05/07 17:12
     */
    @Override
    @Transactional(readOnly = false)
    public long addRole(Role role, String createBy) {
        role.setIsDel(AppConstants.COMMON_BYTE_NO);
        role.setCreateBy(createBy);
        role.setCreateTime(DateUtil.currentSeconds());
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

    /*
     * 编辑角色是否删除
     * @param roleId
     * @param isDel
     * @param updateBy
     * @author admin
     * @date 2025/05/07 17:13
     */
    @Override
    public void modifyRoleIsDelById(long roleId, byte isDel, String updateBy) {
        roleMapper.updateRoleIsDelById(roleId,isDel,updateBy,DateUtil.currentSeconds());
    }

    /*
     * 编辑角色
     * @param role
     * @param updateBy
     * @author admin
     * @date 2025/05/07 17:13
     */
    @Override
    public void modifyRoleById(Role role, String updateBy) {
        role.setUpdateBy(updateBy);
        role.setUpdateTime(DateUtil.currentSeconds());
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

    /*
     * 获取角色详情
     * @param roleId
     * @return com.ren.common.core.entity.Role
     * @author admin
     * @date 2025/05/07 17:14
     */
    @Override
    public Role getRoleById(long roleId) {
        Role role = roleMapper.selectById(roleId);
        return role;
    }

    /*
     * 根据参数获取角色列表
     * @param paramMap
     * @return java.util.List<com.ren.common.core.entity.Role>
     * @author admin
     * @date 2025/05/07 17:15
     */
    @Override
    public IPage<Role> listRoleByPage(Map<String, Object> paramMap) {
        if(paramMap != null && paramMap.containsKey("searchLike") && StrUtil.isNotBlank(Convert.toStr(paramMap.get("searchLike")))){
            paramMap.put("searchLike", StrUtil.format("%%{}%%",paramMap.get("searchLike")));
        }
        IPage<Role> roleList = roleMapper.listRoleByPage(PageUtils.createPage(Role.class),paramMap);
        return roleList;
    }

    /*
     * 根据参数获取角色列表
     * @param paramMap
     * @return java.util.List<com.ren.common.domain.entity.Role>
     * @author admin
     * @date 2025/05/13 20:05
     */
    @Override
    public List<Role> listRoleByParam(Map<String, Object> paramMap) {
        if(paramMap != null && paramMap.containsKey("searchLike") && StrUtil.isNotBlank(Convert.toStr(paramMap.get("searchLike")))){
            paramMap.put("searchLike", StrUtil.format("%%{}%%",paramMap.get("searchLike")));
        }
        return roleMapper.listRoleByParam(paramMap);
    }
}