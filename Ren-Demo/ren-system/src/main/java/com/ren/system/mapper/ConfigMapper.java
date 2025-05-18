package com.ren.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ren.common.domain.entity.Dept;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface DeptMapper extends BaseMapper<Dept> {

    /*
     * 添加部门
     * @param dept
     * @author admin
     * @date 2025/05/07 17:38
     */
    void insertDept(Dept dept);

    /*
     * 编辑部门是否删除
     * @param deptId
     * @param isDel
     * @param updateBy
     * @param updateTime
     * @author admin
     * @date 2025/05/07 17:30
     */
    void updateDeptIsDelById(@Param("deptId")long deptId, @Param("isDel")byte isDel, @Param("updateBy")String updateBy, @Param("updateTime")long updateTime);

    /*
     * 编辑部门
     * @param dept
     * @author admin
     * @date 2025/05/07 17:31
     */
    void updateDeptById(Dept dept);

    /*
     * 根据参数获取部门列表
     * @param paramMap
     * @return java.util.List<com.ren.common.core.entity.Dept>
     * @author admin
     * @date 2025/05/07 17:31
     */
    List<Dept> listDeptByParam(Map<String, Object> paramMap);

}