package com.ren.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ren.common.core.domain.entity.Dept;

import java.util.List;
import java.util.Map;

public interface DeptService extends IService<Dept> {

    /**
     * 添加部门
     * @param createBy
     * @return long
     * @author ren
     * @date 2025/05/07 17:12
     */
    long addDept(Dept dept,String createBy);

    /**
     * 编辑部门是否删除
     * @param deptId
     * @param isDel
     * @param updateBy
     * @author ren
     * @date 2025/05/07 17:13
     */
    void modifyDeptIsDelById(long deptId,byte isDel,String updateBy);

    /**
     * 编辑部门
     * @param dept
     * @param updateBy
     * @author ren
     * @date 2025/05/07 17:13
     */
    void modifyDeptById(Dept dept,String updateBy);

    /**
     * 获取部门详情
     * @param deptId
     * @return com.ren.common.core.entity.Dept
     * @author ren
     * @date 2025/05/07 17:14
     */
    Dept getDeptById(long deptId);

    /**
     * 根据参数获取部门列表
     * @param paramMap
     * @return java.util.List<com.ren.common.core.entity.Dept>
     * @author ren
     * @date 2025/05/07 17:15
     */
    List<Dept> listDeptByParam(Map<String,Object> paramMap);

}