package com.ren.admin.controller;

import com.ren.common.constant.AppConstants;
import com.ren.common.core.dto.AjaxResultDTO;
import com.ren.common.core.entity.Role;
import com.ren.common.core.entity.User;
import com.ren.system.service.RoleService;
import com.ren.system.service.UserRoleService;
import com.ren.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    RoleService roleService;
    @Autowired
    UserService userService;

    /*
     * 角色分页列表
     * @param paramMap
     * @return com.ren.common.dto.AjaxResultDTO
     * @author admin
     * @date 2025/05/08 15:01
     */
    @PostMapping("/list")
    public AjaxResultDTO listRoleByPage(@RequestBody(required = false) Map<String,Object> paramMap) {
        List<Role> roleList = roleService.listRoleByParam(paramMap);
        return AjaxResultDTO.success().put("roleList",roleList);
    }

    /*
     * 角色列表
     * @return com.ren.common.core.dto.AjaxResultDTO
     * @author admin
     * @date 2025/05/12 19:28
     */
    @GetMapping("/list")
    public AjaxResultDTO listRole() {
        List<Role> roleList = roleService.listRoleByParam(null);
        roleList = roleList.stream().filter(role -> !role.getRoleName().equals(AppConstants.ROLE_SUPER_NAME)).collect(Collectors.toList());
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
        //查询当前角色下是否有用户还在使用
        List<User> userList = userService.listUserByRoleId(roleId);
        if(userList != null && !userList.isEmpty()){
            return AjaxResultDTO.error("当前角色下还有用户在使用，不能删除");
        }
        roleService.modifyRoleIsDelById(roleId, AppConstants.COMMON_BYTE_YES,loginUser.getUsername());
        return AjaxResultDTO.success();
    }

}