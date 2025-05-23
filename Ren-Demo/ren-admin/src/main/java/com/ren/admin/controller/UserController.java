package com.ren.admin.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ren.common.constant.AppConstants;
import com.ren.common.controller.BaseController;
import com.ren.common.domain.bo.LoginUser;
import com.ren.common.domain.dto.AjaxResultDTO;
import com.ren.common.domain.entity.Menu;
import com.ren.common.domain.entity.Role;
import com.ren.common.domain.entity.User;
import com.ren.common.domain.enums.BusinessType;
import com.ren.common.domain.page.TableDataInfo;
import com.ren.common.domain.vo.DynamicRouteVO;
import com.ren.common.domain.vo.MenuVO;
import com.ren.common.interfaces.OperLogAnn;
import com.ren.common.interfaces.Pageable;
import com.ren.common.utils.TreeUtils;
import com.ren.system.entity.UserPost;
import com.ren.system.entity.UserRole;
import com.ren.system.service.*;
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
    UserPostService userPostService;
    @Autowired
    RoleService roleService;
    @Autowired
    MenuService menuService;

    /*
     * 用户信息（登录使用）
     * @AuthenticationPrincipal LoginUser loginUser可以直接从SpringSecurity中获取到当前的用户信息
     * @param user
     * @return org.springframework.http.ResponseEntity<?>
     * @author admin
     * @date 2025/04/17 19:42
     */
    @GetMapping("/info")
    public AjaxResultDTO getUserInfo(@AuthenticationPrincipal LoginUser loginUser) {
        //获取用户信息，并返回
        AjaxResultDTO ajax = AjaxResultDTO.success();
        loginUser.getUser().setPassword(null);
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
        ajax.put("permissions", menuList.stream().filter(menu -> !menu.getMenuType().equals(AppConstants.MENU_TYPE_DIR)).map(Menu::getPerms).toArray(String[]::new));
        List<Menu> routerMenuList = menuList.stream().filter(menu -> menu.getMenuType().equals(AppConstants.MENU_TYPE_DIR) || menu.getMenuType().equals(AppConstants.MENU_TYPE_MENU)).toList();
        List<Menu> routerMenuTree = TreeUtils.formatTree(routerMenuList, menu -> Convert.toInt(BeanUtil.getProperty(menu, "parentId")) == 0,"menuId",null,null,null);
        //获取用户所能看到的菜单（包含目录），格式化后返回（前端用于侧边栏目录显示）
        List<MenuVO> menuVOList = routerMenuTree.stream().map(menu -> new MenuVO(menu, "path", "menuName", "icon", "children")).toList();
        //递归修改每一级菜单路径
        menuVOList = modifyMenuVOIndex("",menuVOList);
        ajax.put("menus", menuVOList);
        //获取用户所能看到的菜单（不包含目录），格式化后返回（前端用于动态路由配置）
        List<DynamicRouteVO> dynamicRouteVOList = routerMenuTree.stream().map(menu -> new DynamicRouteVO(menu,"path", "routeName", "component", new DynamicRouteVO().new Meta(true, new String[]{},""),"children")).toList();
        dynamicRouteVOList = modifyDynamicRouteVOMenuShow("",dynamicRouteVOList);
        ajax.put("dynamicRoutes", dynamicRouteVOList);
        return ajax;
    }

    /*
     * 递归修改每级菜单Index值
     * @param parentIndex
     * @param menuVOList
     * @return java.util.List<com.ren.common.domain.vo.MenuVO>
     * @author admin
     * @date 2025/05/19 17:36
     */
    protected List<MenuVO> modifyMenuVOIndex(String parentIndex,List<MenuVO> menuVOList) {
        for(MenuVO menuVO : menuVOList){
            menuVO.setIndex(parentIndex + "/" + menuVO.getIndex());
            if(menuVO.getChildren() != null && !menuVO.getChildren().isEmpty()){
                menuVO.setChildren(modifyMenuVOIndex(menuVO.getIndex(),menuVO.getChildren()));
            }
        }
        return menuVOList;
    }

    /*
     * 递归修改每级菜单Show值
     * @param parentMenuShow
     * @param dynamicRouteVOList
     * @return java.util.List<com.ren.common.domain.vo.DynamicRouteVO>
     * @author admin
     * @date 2025/05/19 17:36
     */
    protected List<DynamicRouteVO> modifyDynamicRouteVOMenuShow(String parentMenuShow,List<DynamicRouteVO> dynamicRouteVOList) {
        for(DynamicRouteVO dynamicRouteVO : dynamicRouteVOList){
            dynamicRouteVO.getMeta().setMenuShow(parentMenuShow + "/" + dynamicRouteVO.getPath());
            if(dynamicRouteVO.getChildren() != null && !dynamicRouteVO.getChildren().isEmpty()){
                dynamicRouteVO.setChildren(modifyDynamicRouteVOMenuShow(dynamicRouteVO.getMeta().getMenuShow(),dynamicRouteVO.getChildren()));
            }
        }
        return dynamicRouteVOList;
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
        List<UserPost> userPostList = userPostService.listUserPostByUserId(userId);
        Long[] postIdArr = userPostList.stream().map(UserPost::getPostId).toArray(Long[]::new);
        return AjaxResultDTO.success().put("userInfo",user).put("roleIdArr",roleIdArr).put("postIdArr",postIdArr);
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
        IPage<User> userList = userService.listUserByPage(paramMap);
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
    @OperLogAnn(title = "用户模块", businessType = BusinessType.INSERT)
    public AjaxResultDTO addUser(@AuthenticationPrincipal LoginUser loginUser, @RequestBody(required = false) User addUser) {
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
    @OperLogAnn(title = "用户模块", businessType = BusinessType.UPDATE)
    public AjaxResultDTO modifyUser(@AuthenticationPrincipal LoginUser loginUser, @RequestBody(required = false) User modifyUser) {
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
    @OperLogAnn(title = "用户模块", businessType = BusinessType.DELETE)
    public AjaxResultDTO deleteUser(@AuthenticationPrincipal LoginUser loginUser, long userId) {
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
    @OperLogAnn(title = "用户模块", businessType = BusinessType.UPDATE)
    public AjaxResultDTO resetPassword(@AuthenticationPrincipal LoginUser loginUser, @RequestBody(required = false) Map<String,Object> paramMap) {
        userService.resetPassword(Convert.toLong(paramMap.get("userId")),Convert.toStr(paramMap.get("password")),loginUser.getUsername());
        return AjaxResultDTO.success();
    }

}