package com.ren.admin.controller.system;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import com.ren.common.controller.BaseController;
import com.ren.common.core.constant.AppConstants;
import com.ren.common.core.domain.entity.Dept;
import com.ren.common.core.domain.entity.User;
import com.ren.common.core.enums.BusinessType;
import com.ren.common.core.interfaces.OperLogAnn;
import com.ren.common.core.domain.bo.LoginUser;
import com.ren.common.core.response.AjaxResult;
import com.ren.common.utils.StringUtils;
import com.ren.common.utils.TreeUtils;
import com.ren.system.entity.RoleDept;
import com.ren.system.service.DeptService;
import com.ren.system.service.RoleDeptService;
import com.ren.system.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/dept")
@Tag(name = "部门相关", description = "部门相关")
public class DeptController extends BaseController {

    @Autowired
    DeptService deptService;
    @Autowired
    RoleDeptService roleDeptService;
    @Autowired
    UserService userService;

    /*
     * 部门树形列表（页面显示用）
     * @param paramMap
     * @return com.ren.common.dto.AjaxResult
     * @author ren
     * @date 2025/05/08 17:14
     */
    @GetMapping("/list/tree")
    @Operation(summary = "部门树形列表", description = "部门树形列表（页面显示用）")
    public AjaxResult listDeptTree(@RequestParam Map<String,Object> paramMap)
    {
        List<Dept> deptList = deptService.listDeptByParam(paramMap);
        //将列表转为树形结构
        deptList = TreeUtils.formatTree(deptList, dept -> Convert.toInt(BeanUtil.getProperty(dept, "parentId")) == 0,"deptId",null,null,null);
        return success().put("deptList",deptList);
    }

    /*
     * 部门树形列表（其他模块下拉列表用）
     * @return com.ren.common.core.dto.AjaxResult
     * @author ren
     * @date 2025/05/10 18:06
     */
    @GetMapping("/list")
    @Operation(summary = "部门树形列表", description = "部门树形列表（其他模块下拉列表用）")
    public AjaxResult listDeptTree()
    {
        List<Dept> deptList = deptService.listDeptByParam(null);
        //将列表转为树形结构
        deptList = TreeUtils.formatTree(deptList, dept -> Convert.toInt(BeanUtil.getProperty(dept, "parentId")) == 0,"deptId",null,null,null);
        return success().put("deptList",TreeUtils.convertTreeSelectForAll(deptList, "deptId", "deptName", "isStop", "children"));
    }

    /*
     * 排除本部门Id的部门列表（部门本身修改时下拉列表使用）
     * @param deptId
     * @return com.ren.common.dto.AjaxResult
     * @author ren
     * @date 2025/05/08 17:14
     */
    @GetMapping("/list/parent/{deptId}")
    @Operation(summary = "排除本部门Id的部门列表", description = "排除本部门Id的部门列表（部门本身修改时下拉列表使用）")
    public AjaxResult listParentDept(@PathVariable(value = "deptId", required = false) Long deptId)
    {
        List<Dept> deptList = deptService.listDeptByParam(null);
        deptList.removeIf(d -> d.getDeptId().intValue() == deptId || CollUtil.contains(
                StringUtils.split(d.getAncestors(), ","),
                String.valueOf(deptId)
        ));
        //将列表转为树形结构
        deptList = TreeUtils.formatTree(deptList,dept -> Convert.toInt(BeanUtil.getProperty(dept, "parentId")) == 0,"deptId","parentId","children",null);
        //将部门列表转换为下拉框树形结构后传输到前台
        return success().put("parentDeptList",TreeUtils.convertTreeSelectForAll(deptList, "deptId", "deptName", "isStop", "children"));
    }

    /*
     * 角色部门权限列表
     * @param roleId
     * @return com.ren.common.core.dto.AjaxResult
     * @author ren
     * @date 2025/05/12 16:18
     */
    @GetMapping("/list/role")
    @Operation(summary = "角色部门权限列表", description = "角色部门权限列表")
    public AjaxResult listRoleDept(Integer roleId)
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

        return success().put("deptList",TreeUtils.convertTreeSelectForAll(deptList, "deptId", "deptName", "isStop", "children")).put("deptIdArr",deptIdArr);
    }

    /*
     * 添加部门
     * @param loginUser
     * @param addDept
     * @return com.ren.common.dto.AjaxResult
     * @author ren
     * @date 2025/05/09 17:01
     */
    @PostMapping("/add")
    @OperLogAnn(title = "部门模块", businessType = BusinessType.INSERT)
    @Operation(summary = "添加部门", description = "添加部门")
    public AjaxResult addDept(@AuthenticationPrincipal LoginUser loginUser, @RequestBody(required = false) Dept addDept) {
        deptService.addDept(addDept,loginUser.getUsername());
        return success();
    }

    /*
     * 编辑部门
     * @param loginUser
     * @param modifyDept
     * @return com.ren.common.dto.AjaxResult
     * @author ren
     * @date 2025/05/09 17:01
     */
    @PostMapping("/modify")
    @OperLogAnn(title = "部门模块", businessType = BusinessType.UPDATE)
    @Operation(summary = "编辑部门", description = "编辑部门")
    public AjaxResult modifyDept(@AuthenticationPrincipal LoginUser loginUser, @RequestBody(required = false) Dept modifyDept) {
        deptService.modifyDeptById(modifyDept,loginUser.getUsername());
        return success();
    }

    /*
     * 删除部门
     * @param loginUser
     * @param deptId
     * @return com.ren.common.dto.AjaxResult
     * @author ren
     * @date 2025/05/09 17:01
     */
    @DeleteMapping("/delete")
    @OperLogAnn(title = "部门模块", businessType = BusinessType.DELETE)
    @Operation(summary = "删除部门", description = "删除部门")
    public AjaxResult deptDelete(@AuthenticationPrincipal LoginUser loginUser, long deptId) {
        //查询是否有子级部门
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("parentId",deptId);
        List<Dept> deptList = deptService.listDeptByParam(paramMap);
        if(deptList != null && !deptList.isEmpty()){
            return warn("请先删除子级部门");
        }
        //查询部门下是否有用户
        List<User> userList = userService.listUserByDeptId(deptId);
        if(userList != null && !userList.isEmpty()){
            return warn("该部门下还有正在使用的用户，请先删除");
        }
        deptService.modifyDeptIsDelById(deptId, AppConstants.COMMON_BYTE_YES,loginUser.getUsername());
        return success();
    }

}