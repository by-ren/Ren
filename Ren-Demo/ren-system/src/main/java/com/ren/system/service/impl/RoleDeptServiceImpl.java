
package com.ren.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ren.system.entity.RoleDept;
import com.ren.system.mapper.RoleDeptMapper;
import com.ren.system.service.RoleDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleDeptServiceImpl extends ServiceImpl<RoleDeptMapper, RoleDept> implements RoleDeptService {

    @Autowired
    private RoleDeptMapper roleDeptMapper;

    /*
     * 添加可查看数据部门
     * @param roleDept
     * @author admin
     * @date 2025/05/07 17:17
     */
    @Override
    public void addRoleDept(RoleDept roleDept) {
        roleDeptMapper.insertRoleDept(roleDept);
    }

    /*
     * 批量添加数据部门
     * @param roleDeptList
     * @author admin
     * @date 2025/05/12 16:11
     */
    @Override
    public void addRoleDeptBatch(List<RoleDept> roleDeptList) {
        roleDeptMapper.insertRoleDeptBatch(roleDeptList);
    }

    /*
     * 删除可查看数据部门
     * @param roleId
     * @author admin
     * @date 2025/05/07 17:18
     */
    @Override
    public void removeRoleDeptByRoleId(long roleId) {
        roleDeptMapper.deleteRoleDeptByRoleId(roleId);
    }

    /*
     * 获取可查看数据部门列表
     * @param roleId
     * @return java.util.List<com.ren.system.entity.RoleDept>
     * @author admin
     * @date 2025/05/07 17:19
     */
    @Override
    public List<RoleDept> listRoleDeptByRoleId(long roleId) {
        List<RoleDept> roleDeptList = roleDeptMapper.listRoleDeptByRoleId(roleId);
        return roleDeptList;
    }
}