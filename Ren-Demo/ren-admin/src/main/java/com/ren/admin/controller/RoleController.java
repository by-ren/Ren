package com.ren.admin.controller;

import com.ren.common.constant.AppConstants;
import com.ren.common.core.dto.AjaxResultDTO;
import com.ren.common.core.entity.Role;
import com.ren.common.core.entity.User;
import com.ren.system.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    RoleService roleService;

    /*
     * 角色列表
     * @param paramMap
     * @return com.ren.common.dto.AjaxResultDTO
     * @author admin
     * @date 2025/05/08 15:01
     */
    @PostMapping("/list")
    public AjaxResultDTO getRoleList(@RequestBody(required = false) Map<String,Object> paramMap) {
        List<Role> roleList = roleService.listRoleByParam(paramMap);
        return AjaxResultDTO.success().put("roleList",roleList);
    }

    /*
     * 添加角色
     * @param loginUser
     * @param addRole
     * @return com.ren.common.dto.AjaxResultDTO
     * @author admin
     * @date 2025/05/08 15:01
     */
    @PostMapping("/add")
    public AjaxResultDTO addRole(@AuthenticationPrincipal User loginUser, @RequestBody(required = false) Role addRole) {
        roleService.addRole(addRole,loginUser.getUsername());
        return AjaxResultDTO.success();
    }

    /*
     * 编辑角色
     * @param loginUser
     * @param modifyRole
     * @return com.ren.common.dto.AjaxResultDTO
     * @author admin
     * @date 2025/05/08 15:32
     */
    @PostMapping("/modify")
    public AjaxResultDTO modifyRole(@AuthenticationPrincipal User loginUser, @RequestBody(required = false) Role modifyRole) {
        roleService.modifyRoleById(modifyRole,loginUser.getUsername());
        return AjaxResultDTO.success();
    }

    /*
     * 删除角色
     * @param loginUser
     * @param userId
     * @return com.ren.common.dto.AjaxResultDTO
     * @author admin
     * @date 2025/05/08 15:32
     */
    @DeleteMapping("/delete")
    public AjaxResultDTO roleDelete(@AuthenticationPrincipal User loginUser, long roleId) {
        roleService.modifyRoleIsDelById(roleId, AppConstants.COMMON_BYTE_YES,loginUser.getUsername());
        return AjaxResultDTO.success();
    }

}