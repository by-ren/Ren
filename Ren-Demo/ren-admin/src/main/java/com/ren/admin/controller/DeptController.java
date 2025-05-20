package com.ren.admin.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.ren.common.constant.AppConstants;
import com.ren.common.domain.bo.LoginUser;
import com.ren.common.domain.dto.AjaxResultDTO;
import com.ren.common.domain.entity.Dept;
import com.ren.common.domain.entity.User;
import com.ren.common.utils.TreeUtils;
import com.ren.system.entity.RoleDept;
import com.ren.system.service.DeptService;
import com.ren.system.service.RoleDeptService;
import com.ren.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/dept")
public class DeptController {

    @Autowired
    DeptService deptService;
    @Autowired
    RoleDeptService roleDeptService;
    @Autowired
    UserService userService;

    /*
     * 部门树形列表（页面显示用）
     * @param paramMap
     * @return com.ren.common.dto.AjaxResultDTO
     * @author admin
     * @date 2025/05/08 17:14
     */
    @GetMapping("/list/tree")
    public AjaxResultDTO listDeptTree(@RequestParam Map<String,Object> paramMap)
    {
        List<Dept> deptList = deptService.listDeptByParam(paramMap);
        //将列表转为树形结构
        deptList = TreeUtils.formatTree(deptList, dept -> Convert.toInt(BeanUtil.getProperty(dept, "parentId")) == 0,"deptId",null,null,null);
        return AjaxResultDTO.success().put("deptList",deptList);
    }

    /*
     * 部门树形列表（其他模块下拉列表用）
     * @return com.ren.common.core.dto.AjaxResultDTO
     * @author admin
     * @date 2025/05/10 18:06
     */
    @GetMapping("/list")
    public AjaxResultDTO listDeptTree()
    {
        List<Dept> deptList = deptService.listDeptByParam(null);
        //将列表转为树形结构
        deptList = TreeUtils.formatTree(deptList, dept -> Convert.toInt(BeanUtil.getProperty(dept, "parentId")) == 0,"deptId",null,null,null);
        return AjaxResultDTO.success().put("deptList",TreeUtils.convertTreeSelectForAll(deptList, "deptId", "deptName", "isStop", "children"));
    }

    /*
     * 排除本部门Id的部门列表（部门本身修改时下拉列表使用）
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
        deptList = TreeUtils.formatTree(deptList,dept -> Convert.toInt(BeanUtil.getProperty(dept, "parentId")) == 0,"deptId","parentId","children",null);
        //将部门列表转换为下拉框树形结构后传输到前台
        return AjaxResultDTO.success().put("parentDeptList",TreeUtils.convertTreeSelectForAll(deptList, "deptId", "deptName", "isStop", "children"));
    }

    /*
     * 角色部门权限列表
     * @param roleId
     * @return com.ren.common.core.dto.AjaxResultDTO
     * @author admin
     * @date 2025/05/12 16:18
     */
    @GetMapping("/list/role")
    public AjaxResultDTO listRoleDept(Integer roleId)
    {
        List<Dept> deptList = deptService.listDeptByParam(null);
        //将列表转为树形结构
        deptList = TreeUtils.formatTree(deptList, dept -> Convert.toInt(BeanUtil.getProperty(dept, "parentId")) == 0,"deptId",null,null,"orderNum");

        Long[] deptIdArr = null;
        if(roleId != null){
            //获取角色菜单列表
            List<RoleDept> roleDeptList = roleDeptService.listRoleDeptByRoleId(roleId);
            if(roleDeptList != null && !roleDeptList.isEmpty()){
                deptIdArr = roleDeptList.stream().map(roleDept -> roleDept.getDeptId()).toArray(Long[] :: new);
            }
        }

        return AjaxResultDTO.success().put("deptList",TreeUtils.convertTreeSelectForAll(deptList, "deptId", "deptName", "isStop", "children")).put("deptIdArr",deptIdArr);
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
    public AjaxResultDTO addDept(@AuthenticationPrincipal LoginUser loginUser, @RequestBody(required = false) Dept addDept) {
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
    public AjaxResultDTO modifyDept(@AuthenticationPrincipal LoginUser loginUser, @RequestBody(required = false) Dept modifyDept) {
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
    public AjaxResultDTO deptDelete(@AuthenticationPrincipal LoginUser loginUser, long deptId) {
        //查询是否有子级部门
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("parentId",deptId);
        List<Dept> deptList = deptService.listDeptByParam(paramMap);
        if(deptList != null && !deptList.isEmpty()){
            return AjaxResultDTO.warn("请先删除子级部门");
        }
        //查询部门下是否有用户
        List<User> userList = userService.listUserByDeptId(deptId);
        if(userList != null && !userList.isEmpty()){
            return AjaxResultDTO.warn("该部门下还有正在使用的用户，请先删除");
        }
        deptService.modifyDeptIsDelById(deptId, AppConstants.COMMON_BYTE_YES,loginUser.getUsername());
        return AjaxResultDTO.success();
    }

}