
package com.ren.system.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ren.common.constant.AppConstants;
import com.ren.common.core.entity.Role;
import com.ren.system.mapper.RoleMapper;
import com.ren.system.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    /*
     * 添加角色
     * @param createBy
     * @return long
     * @author admin
     * @date 2025/05/07 17:12
     */
    @Override
    public long addRole(Role role, String createBy) {
        role.setIsDel(AppConstants.COMMON_BYTE_NO);
        role.setCreateBy(createBy);
        role.setCreateTime(DateUtil.currentSeconds());
        roleMapper.insertRole(role);
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
    public List<Role> listRoleByParam(Map<String, Object> paramMap) {
        if(paramMap != null && paramMap.containsKey("searchLike") && StrUtil.isNotBlank(Convert.toStr(paramMap.get("searchLike")))){
            paramMap.put("searchLike", StrUtil.format("%%{}%%",paramMap.get("searchLike")));
        }
        List<Role> roleList = roleMapper.listRoleByParam(paramMap);
        return roleList;
    }
}