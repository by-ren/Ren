package com.ren.admin.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.ren.common.constant.AppConstants;
import com.ren.common.core.dto.AjaxResultDTO;
import com.ren.common.core.entity.Dept;
import com.ren.common.core.entity.User;
import com.ren.common.utils.TreeUtils;
import com.ren.system.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/dept")
public class DeptController {

    @Autowired
    DeptService deptService;

    /*
     * 获取部门树形列表
     * @param paramMap
     * @return com.ren.common.dto.AjaxResultDTO
     * @author admin
     * @date 2025/05/08 17:14
     */
    @PostMapping("/list")
    public AjaxResultDTO list(@RequestBody(required = false) Map<String,Object> paramMap)
    {
        List<Dept> deptList = deptService.listDeptByParam(paramMap);
        //将列表转为树形结构
        deptList = TreeUtils.formatTree(deptList, dept -> Convert.toInt(BeanUtil.getProperty(dept, "parentId")) == 0,"deptId",null,null,"orderNum");
        return AjaxResultDTO.success().put("deptList",deptList);
    }

    /*
     * 部门树形列表
     * @return com.ren.common.core.dto.AjaxResultDTO
     * @author admin
     * @date 2025/05/10 18:06
     */
    @GetMapping("/list")
    public AjaxResultDTO listDept()
    {
        List<Dept> deptList = deptService.listDeptByParam(null);
        //将列表转为树形结构
        deptList = TreeUtils.formatTree(deptList, dept -> Convert.toInt(BeanUtil.getProperty(dept, "parentId")) == 0,"deptId",null,null,"orderNum");
        return AjaxResultDTO.success().put("deptList",TreeUtils.convertTreeSelectForAll(deptList, "deptId", "deptName", "isStop", "children"));
    }

    /*
     * 获取排除本部门Id的部门列表（修改时使用）
     * @param deptId
     * @return com.ren.common.dto.AjaxResultDTO
     * @author admin
     * @date 2025/05/08 17:14
     */
    @GetMapping("/list/parent/{deptId}")
    public AjaxResultDTO listParentDept(@PathVariable(value = "deptId", required = false) Long deptId)
    {
        List<Dept> deptList = deptService.listDeptByParam(null);
        deptList.removeIf(d -> d.getDeptId().intValue() == deptId || CollUtil.contains(
                StrUtil.split(d.getAncestors(), ","),
                String.valueOf(deptId)
        ));
        //将列表转为树形结构
        deptList = TreeUtils.formatTree(deptList,dept -> Convert.toInt(BeanUtil.getProperty(dept, "parentId")) == 0,"deptId","parentId","children","orderNum");
        //将部门列表转换为下拉框树形结构后传输到前台
        return AjaxResultDTO.success().put("parentDeptList",TreeUtils.convertTreeSelectForAll(deptList, "deptId", "deptName", "isStop", "children"));
    }

    /*
     * 添加部门
     * @param loginUser
     * @param addDept
     * @return com.ren.common.dto.AjaxResultDTO
     * @author admin
     * @date 2025/05/09 17:01
     */
    @PostMapping("/add")
    public AjaxResultDTO addDept(@AuthenticationPrincipal User loginUser, @RequestBody(required = false) Dept addDept) {
        deptService.addDept(addDept,loginUser.getUsername());
        return AjaxResultDTO.success();
    }

    /*
     * 编辑部门
     * @param loginUser
     * @param modifyDept
     * @return com.ren.common.dto.AjaxResultDTO
     * @author admin
     * @date 2025/05/09 17:01
     */
    @PostMapping("/modify")
    public AjaxResultDTO modifyDept(@AuthenticationPrincipal User loginUser, @RequestBody(required = false) Dept modifyDept) {
        deptService.modifyDeptById(modifyDept,loginUser.getUsername());
        return AjaxResultDTO.success();
    }

    /*
     * 删除部门
     * @param loginUser
     * @param deptId
     * @return com.ren.common.dto.AjaxResultDTO
     * @author admin
     * @date 2025/05/09 17:01
     */
    @DeleteMapping("/delete")
    public AjaxResultDTO deptDelete(@AuthenticationPrincipal User loginUser, long deptId) {
        deptService.modifyDeptIsDelById(deptId, AppConstants.COMMON_BYTE_YES,loginUser.getUsername());
        return AjaxResultDTO.success();
    }

}