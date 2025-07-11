package com.ren.system.service.impl;

import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ren.common.core.constant.AppConstants;
import com.ren.common.core.domain.entity.Dept;
import com.ren.common.utils.DateUtils;
import com.ren.common.utils.StringUtils;
import com.ren.system.mapper.DeptMapper;
import com.ren.system.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DeptServiceImpl extends ServiceImpl<DeptMapper, Dept> implements DeptService {

    @Autowired
    private DeptMapper deptMapper;

    /**
     * 添加部门
     * @param createBy
     * @return long
     * @author ren
     * @date 2025/05/07 17:12
     */
    @Override
    public long addDept(Dept dept, String createBy) {
        dept.setIsDel(AppConstants.COMMON_BYTE_NO);
        dept.setCreateBy(createBy);
        dept.setCreateTime(DateUtils.currentSeconds());
        deptMapper.insertDept(dept);
        return dept.getDeptId();
    }

    /**
     * 编辑部门是否删除
     * @param deptId
     * @param isDel
     * @param updateBy
     * @author ren
     * @date 2025/05/07 17:13
     */
    @Override
    public void modifyDeptIsDelById(long deptId, byte isDel, String updateBy) {
        deptMapper.updateDeptIsDelById(deptId,isDel,updateBy,DateUtils.currentSeconds());
    }

    /**
     * 编辑部门
     * @param dept
     * @param updateBy
     * @author ren
     * @date 2025/05/07 17:13
     */
    @Override
    public void modifyDeptById(Dept dept, String updateBy) {
        dept.setUpdateBy(updateBy);
        dept.setUpdateTime(DateUtils.currentSeconds());
        deptMapper.updateDeptById(dept);
    }

    /**
     * 获取部门详情
     * @param deptId
     * @return com.ren.common.core.entity.Dept
     * @author ren
     * @date 2025/05/07 17:14
     */
    @Override
    public Dept getDeptById(long deptId) {
        Dept dept = deptMapper.selectById(deptId);
        return dept;
    }

    /**
     * 根据参数获取部门列表
     * @param paramMap
     * @return java.util.List<com.ren.common.core.entity.Dept>
     * @author ren
     * @date 2025/05/07 17:15
     */
    @Override
    public List<Dept> listDeptByParam(Map<String, Object> paramMap) {
        if(paramMap != null && paramMap.containsKey("searchLike") && StringUtils.isNotBlank(Convert.toStr(paramMap.get("searchLike")))){
            paramMap.put("searchLike", StringUtils.format("%%{}%%",paramMap.get("searchLike")));
        }
        List<Dept> deptList = deptMapper.listDeptByParam(paramMap);
        return deptList;
    }
}