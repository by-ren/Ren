package com.ren.admin.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ren.common.constant.AppConstants;
import com.ren.common.controller.BaseController;
import com.ren.common.domain.dto.AjaxResultDTO;
import com.ren.common.domain.entity.Menu;
import com.ren.common.domain.entity.Role;
import com.ren.common.domain.entity.User;
import com.ren.common.domain.page.TableDataInfo;
import com.ren.common.domain.vo.DynamicRouteVO;
import com.ren.common.domain.vo.MenuVO;
import com.ren.common.interfaces.Pageable;
import com.ren.common.utils.TreeUtils;
import com.ren.system.entity.UserRole;
import com.ren.system.service.MenuService;
import com.ren.system.service.RoleService;
import com.ren.system.service.UserRoleService;
import com.ren.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController extends BaseController {

    @Autowired
    UserService userService;
    @Autowired
    UserRoleService userRoleService;
    @Autowired
    RoleService roleService;
    @Autowired
    MenuService menuService;

    /*
     * 用户信息（登录使用）
     * @AuthenticationPrincipal User loginUser可以直接从SpringSecurity中获取到当前的用户信息
     * @param user
     * @return org.springframework.http.ResponseEntity<?>
     * @author admin
     * @date 2025/04/17 19:42
     */
    @GetMapping("/info")
    public AjaxResultDTO getUserInfo(@AuthenticationPrincipal User loginUser) {
        //获取用户信息，并返回
        AjaxResultDTO ajax = AjaxResultDTO.success();
        ajax.put("user", loginUser);
        List<Role> roleList = roleService.listRoleByUserId(loginUser.getUserId());
        //是否Admin
        boolean isAdmin = roleList.stream().anyMatch(role -> role.getRoleKey().equals(AppConstants.ROLE_SUPER_KEY));
        ajax.put("isAdmin", isAdmin);
        //获取用户所拥有角色名称，并返回
        ajax.put("roles", roleList.stream().map(Role::getRoleName).toArray(String[]::new));
        //获取用户所拥有角色Id，并返回
        Long[] roleIds = roleList.stream().map(Role::getRoleId).toArray(Long[]::new);
        ajax.put("roleIds", roleIds);
        //获取用所拥有权限，并返回（前端用于判断按钮权限）
        List<Menu> menuList = new ArrayList<>();
        if(isAdmin){
            //超级管理员获取所有菜单
            menuList = menuService.listMenuByParam(null);
        }else if(roleIds.length > 0){
            //不是超级管理员获取相应菜单
            menuList = menuService.listMenuByRoleIds(roleIds);
        }
        ajax.put("permissions", menuList.stream().filter(menu -> menu.getMenuType().equals(AppConstants.MENU_TYPE_BUTTON)).map(Menu::getPerms).toArray(String[]::new));
        //获取用户所能看到的菜单（包含目录），格式化后返回（前端用于侧边栏目录显示）
        List<Menu> sidebarMenu = menuList.stream().filter(menu -> menu.getMenuType().equals(AppConstants.MENU_TYPE_DIR) || menu.getMenuType().equals(AppConstants.MENU_TYPE_MENU)).toList();
        List<Menu> sidebarMenuTree = TreeUtils.formatTree(sidebarMenu, menu -> Convert.toInt(BeanUtil.getProperty(menu, "parentId")) == 0,"menuId",null,null,null);
        List<MenuVO> menuVOList = sidebarMenuTree.stream().map(menu -> new MenuVO(menu, "path", "menuName", "icon", "children")).toList();
        ajax.put("menus", menuVOList);
        //获取用户所能看到的菜单（不包含目录），格式化后返回（前端用于动态路由配置）
        List<Menu> routerrMenu = menuList.stream().filter(menu -> menu.getMenuType().equals(AppConstants.MENU_TYPE_MENU)).toList();
        List<DynamicRouteVO> dynamicRouteVOList = routerrMenu.stream().map(menu -> new DynamicRouteVO(menu.getPath(), menu.getRouteName(), menu.getComponent(), new DynamicRouteVO().new Meta(true, new String[]{}))).toList();
        ajax.put("dynamicRoutes", dynamicRouteVOList);
        return ajax;
    }

    /*
     * 用户信息（其他模块获取用户信息使用）
     * @param userId
     * @return com.ren.common.core.dto.AjaxResultDTO
     * @author admin
     * @date 2025/05/12 21:02
     */
    @GetMapping("/info/v2")
    public AjaxResultDTO getUserInfoV2(Long userId) {
        User user = userService.getUserById(userId);
        List<UserRole> userRoleList = userRoleService.listUserRoleByUserId(userId);
        Long[] roleIdArr = userRoleList.stream().map(UserRole::getRoleId).toArray(Long[]::new);
        return AjaxResultDTO.success().put("userInfo",user).put("roleIdArr",roleIdArr);
    }

    /*
     * 用户列表
     * @return com.ren.admin.common.dto.AjaxResult
     * @author admin
     * @date 2025/04/26 15:55
     */
    @GetMapping("/list/page")
    @Pageable  //注意，如果要开启分页，请添加该注解
    public TableDataInfo listUserByPage(@RequestParam Map<String,Object> paramMap) {
        IPage<User> userList = userService.listUserByParam(paramMap);
        return getDataTable(userList);
    }

    /*
     * 添加用户
     * @param loginUser
     * @param addUser
     * @return com.ren.admin.common.dto.AjaxResult
     * @author admin
     * @date 2025/05/04 16:26
     */
    @PostMapping("/add")
    public AjaxResultDTO addUser(@AuthenticationPrincipal User loginUser, @RequestBody(required = false) User addUser) {
        userService.addUser(addUser,loginUser.getUsername());
        return AjaxResultDTO.success();
    }

    /*
     * 编辑用户
     * @param loginUser
     * @param modifyUser
     * @return com.ren.admin.common.dto.AjaxResult
     * @author admin
     * @date 2025/05/04 16:08
     */
    @PostMapping("/modify")
    public AjaxResultDTO modifyUser(@AuthenticationPrincipal User loginUser, @RequestBody(required = false) User modifyUser) {
        userService.modifyUser(modifyUser,loginUser.getUsername());
        return AjaxResultDTO.success();
    }

    /*
     * 删除用户
     * @param user
     * @param userId
     * @return com.ren.admin.common.dto.AjaxResult
     * @author admin
     * @date 2025/05/04 15:27
     */
    @DeleteMapping("/delete")
    public AjaxResultDTO deleteUser(@AuthenticationPrincipal User loginUser, long userId) {
        userService.modifyUserIsDelById(userId, AppConstants.COMMON_BYTE_YES,loginUser.getUsername());
        return AjaxResultDTO.success();
    }

    /*
     * 重置密码
     * @param user
     * @param paramMap
     * @return com.ren.admin.common.dto.AjaxResult
     * @author admin
     * @date 2025/05/04 14:33
     */
    @PostMapping("/resetPassword")
    public AjaxResultDTO resetPassword(@AuthenticationPrincipal User loginUser, @RequestBody(required = false) Map<String,Object> paramMap) {
        userService.resetPassword(Convert.toLong(paramMap.get("userId")),Convert.toStr(paramMap.get("password")),loginUser.getUsername());
        return AjaxResultDTO.success();
    }

}