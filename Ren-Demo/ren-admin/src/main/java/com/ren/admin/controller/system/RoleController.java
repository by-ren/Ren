package com.ren.admin.controller.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ren.common.core.constant.AppConstants;
import com.ren.common.controller.BaseController;
import com.ren.common.core.domain.bo.LoginUser;
import com.ren.common.core.response.AjaxResult;
import com.ren.common.core.domain.entity.Role;
import com.ren.common.core.domain.entity.User;
import com.ren.common.core.enums.BusinessType;
import com.ren.common.core.page.TableDataInfo;
import com.ren.common.core.interfaces.OperLogAnn;
import com.ren.common.core.interfaces.Pageable;
import com.ren.system.service.RoleService;
import com.ren.system.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/role")
@Tag(name = "角色相关", description = "角色相关")
public class RoleController extends BaseController {

    @Autowired
    RoleService roleService;
    @Autowired
    UserService userService;

    /*
     * 角色分页列表
     * @param paramMap
     * @return com.ren.common.dto.AjaxResult
     * @author ren
     * @date 2025/05/08 15:01
     */
    @GetMapping("/list/page")
    @Pageable  //注意，如果要开启分页，请添加该注解
    @Operation(summary = "角色分页列表", description = "角色分页列表")
    public TableDataInfo listRoleByPage(@RequestParam Map<String,Object> paramMap) {
        IPage<Role> roleList = roleService.listRoleByPage(paramMap);
        return getDataTable(roleList);
    }

    /*
     * 角色列表
     * @return com.ren.common.core.dto.AjaxResult
     * @author ren
     * @date 2025/05/12 19:28
     */
    @GetMapping("/list")
    @Operation(summary = "角色列表", description = "角色列表")
    public AjaxResult listRole() {
        List<Role> roleList = roleService.listRoleByParam(null);
        roleList = roleList.stream().filter(role -> !role.getRoleKey().equals(AppConstants.ROLE_SUPER_KEY)).collect(Collectors.toList());
        return success().put("roleList",roleList);
    }

    /*
     * 添加角色
     * @param loginUser
     * @param addRole
     * @return com.ren.common.dto.AjaxResult
     * @author ren
     * @date 2025/05/08 15:01
     */
    @PostMapping("/add")
    @OperLogAnn(title = "角色模块", businessType = BusinessType.INSERT)
    @Operation(summary = "添加角色", description = "添加角色")
    public AjaxResult addRole(@AuthenticationPrincipal LoginUser loginUser, @RequestBody(required = false) Role addRole) {
        roleService.addRole(addRole,loginUser.getUsername());
        return success();
    }

    /*
     * 编辑角色
     * @param loginUser
     * @param modifyRole
     * @return com.ren.common.dto.AjaxResult
     * @author ren
     * @date 2025/05/08 15:32
     */
    @PostMapping("/modify")
    @OperLogAnn(title = "角色模块", businessType = BusinessType.UPDATE)
    @Operation(summary = "编辑角色", description = "编辑角色")
    public AjaxResult modifyRole(@AuthenticationPrincipal LoginUser loginUser, @RequestBody(required = false) Role modifyRole) {
        roleService.modifyRoleById(modifyRole,loginUser.getUsername());
        return success();
    }

    /*
     * 删除角色
     * @param loginUser
     * @param userId
     * @return com.ren.common.dto.AjaxResult
     * @author ren
     * @date 2025/05/08 15:32
     */
    @DeleteMapping("/delete")
    @OperLogAnn(title = "角色模块", businessType = BusinessType.DELETE)
    @Operation(summary = "删除角色", description = "删除角色")
    public AjaxResult roleDelete(@AuthenticationPrincipal LoginUser loginUser, long roleId) {
        //查询当前角色下是否有用户还在使用
        List<User> userList = userService.listUserByRoleId(roleId);
        if(userList != null && !userList.isEmpty()){
            return error("当前角色下还有用户在使用，不能删除");
        }
        roleService.modifyRoleIsDelById(roleId, AppConstants.COMMON_BYTE_YES,loginUser.getUsername());
        return success();
    }

}