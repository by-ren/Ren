package com.ren.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ren.system.entity.RoleDept;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RoleDeptMapper extends BaseMapper<RoleDept> {

    /*
     * 添加可查看数据部门
     * @param roleDept
     * @author ren
     * @date 2025/05/07 17:17
     */
    void insertRoleDept(RoleDept roleDept);

    /*
     * 删除可查看数据部门
     * @param roleId
     * @author ren
     * @date 2025/05/07 17:18
     */
    void deleteRoleDeptByRoleId(long roleId);

    /*
     * 获取可查看数据部门列表
     * @param roleId
     * @return java.util.List<com.ren.system.entity.RoleDept>
     * @author ren
     * @date 2025/05/07 17:19
     */
    List<RoleDept> listRoleDeptByRoleId(long roleId);

    /*
     * 批量添加数据部门
     * @param roleDeptList
     * @author ren
     * @date 2025/05/12 16:12
     */
	void insertRoleDeptBatch(List<RoleDept> roleDeptList);
}