package com.ren.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ren.system.entity.RoleDept;

import java.util.List;

public interface RoleDeptService extends IService<RoleDept> {

    /**
     * 添加可查看数据部门
     * @param roleDept
     * @author ren
     * @date 2025/05/07 17:17
     */
    void addRoleDept(RoleDept roleDept);

    /**
     * 批量添加数据部门
     * @param roleDeptList
     * @author ren
     * @date 2025/05/12 16:11
     */
    void addRoleDeptBatch(List<RoleDept> roleDeptList);

    /**
     * 删除可查看数据部门
     * @param roleId
     * @author ren
     * @date 2025/05/07 17:18
     */
    void removeRoleDeptByRoleId(long roleId);

    /**
     * 获取可查看数据部门列表
     * @param roleId
     * @return java.util.List<com.ren.system.entity.RoleDept>
     * @author ren
     * @date 2025/05/07 17:19
     */
    List<RoleDept> listRoleDeptByRoleId(long roleId);

}