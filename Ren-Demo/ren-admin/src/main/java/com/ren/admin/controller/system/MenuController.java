package com.ren.admin.controller.system;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import com.ren.common.constant.AppConstants;
import com.ren.common.domain.model.bo.LoginUser;
import com.ren.common.domain.model.dto.AjaxResultDTO;
import com.ren.common.domain.entity.Menu;
import com.ren.common.domain.enums.BusinessType;
import com.ren.common.domain.model.vo.TreeSelectVO;
import com.ren.common.interfaces.OperLogAnn;
import com.ren.common.utils.TreeUtils;
import com.ren.system.entity.RoleMenu;
import com.ren.system.service.MenuService;
import com.ren.system.service.RoleMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    MenuService menuService;
    @Autowired
    RoleMenuService roleMenuService;

    /*
     * 菜单树形列表（页面显示用）
     * @param paramMap
     * @return com.ren.common.dto.AjaxResultDTO
     * @author ren
     * @date 2025/05/08 17:14
     */
    @GetMapping("/list/tree")
    public AjaxResultDTO listMenuTree(@RequestParam Map<String,Object> paramMap)
    {
        List<Menu> menuList = menuService.listMenuByParam(paramMap);
        //将列表转为树形结构
        menuList = TreeUtils.formatTree(menuList, menu -> Convert.toInt(BeanUtil.getProperty(menu, "parentId")) == 0,"menuId",null,null,"orderNum");
        return AjaxResultDTO.success().put("menuList",menuList);
    }

    /*
     * 排除本菜单Id的菜单列表（菜单本身修改时下拉列表使用）
     * @param menuId
     * @return com.ren.common.dto.AjaxResultDTO
     * @author ren
     * @date 2025/05/08 17:14
     */
    @GetMapping("/list/parent/{menuId}")
    public AjaxResultDTO listParentMenuTree(@PathVariable(value = "menuId", required = false) Long menuId)
    {
        List<Menu> menuList = menuService.listMenuByParam(null);
        menuList.removeIf(d -> d.getMenuId().intValue() == menuId);
        //将列表转为树形结构
        menuList = TreeUtils.formatTree(menuList,menu -> Convert.toInt(BeanUtil.getProperty(menu, "parentId")) == 0,"menuId","parentId","children","orderNum");
        //将菜单列表转换为下拉框树形结构后传输到前台
        List<TreeSelectVO> treeSelectVOList =  TreeUtils.convertTreeSelectForAll(menuList, "menuId", "menuName", "isStop", "children");
        TreeSelectVO mianSelect = new TreeSelectVO(0L,"主目录",false,treeSelectVOList);
        List<TreeSelectVO> mianTree = new ArrayList<>();
        mianTree.add(mianSelect);
        return AjaxResultDTO.success().put("parentMenuList",mianTree);
    }

    /*
     * 角色菜单列表（其他模块下拉列表使用）
     * @return com.ren.common.core.dto.AjaxResultDTO
     * @author ren
     * @date 2025/05/12 14:53
     */
    @GetMapping("/list/role")
    public AjaxResultDTO listRoleMenuTree(Integer roleId)
    {
        List<Menu> menuList = menuService.listMenuByParam(null);
        //将列表转为树形结构
        menuList = TreeUtils.formatTree(menuList,menu -> Convert.toInt(BeanUtil.getProperty(menu, "parentId")) == 0,"menuId","parentId","children","orderNum");
        //将菜单列表转换为下拉框树形结构后传输到前台
        List<TreeSelectVO> treeSelectVOList =  TreeUtils.convertTreeSelectForAll(menuList, "menuId", "menuName", "isStop", "children");

        Long[] menuIdArr = null;
        if(roleId != null){
            //获取角色菜单列表
            List<RoleMenu> roleMenuList = roleMenuService.listRoleMenuByRoleId(roleId);
            if(roleMenuList != null && !roleMenuList.isEmpty()){
                menuIdArr = roleMenuList.stream().map(roleMenu -> roleMenu.getMenuId()).toArray(Long[] :: new);
            }
        }

        return AjaxResultDTO.success().put("menuList",treeSelectVOList).put("menuIdArr",menuIdArr);
    }

    /*
     * 添加菜单
     * @param loginUser
     * @param addMenu
     * @return com.ren.common.dto.AjaxResultDTO
     * @author ren
     * @date 2025/05/09 17:01
     */
    @PostMapping("/add")
    @OperLogAnn(title = "菜单模块", businessType = BusinessType.INSERT)
    public AjaxResultDTO addMenu(@AuthenticationPrincipal LoginUser loginUser, @RequestBody(required = false) Menu addMenu) {
        menuService.addMenu(addMenu,loginUser.getUsername());
        return AjaxResultDTO.success();
    }

    /*
     * 编辑菜单
     * @param loginUser
     * @param modifyMenu
     * @return com.ren.common.dto.AjaxResultDTO
     * @author ren
     * @date 2025/05/09 17:01
     */
    @PostMapping("/modify")
    @OperLogAnn(title = "菜单模块", businessType = BusinessType.UPDATE)
    public AjaxResultDTO modifyMenu(@AuthenticationPrincipal LoginUser loginUser, @RequestBody(required = false) Menu modifyMenu) {
        menuService.modifyMenuById(modifyMenu,loginUser.getUsername());
        return AjaxResultDTO.success();
    }

    /*
     * 删除菜单
     * @param loginUser
     * @param menuId
     * @return com.ren.common.dto.AjaxResultDTO
     * @author ren
     * @date 2025/05/09 17:01
     */
    @DeleteMapping("/delete")
    @OperLogAnn(title = "菜单模块", businessType = BusinessType.DELETE)
    public AjaxResultDTO menuDelete(@AuthenticationPrincipal LoginUser loginUser, long menuId) {
        //查询是否有子级菜单
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("parentId",menuId);
        List<Menu> menuList = menuService.listMenuByParam(paramMap);
        if(menuList != null && !menuList.isEmpty()){
            return AjaxResultDTO.warn("请先删除子级菜单");
        }
        //删除菜单
        menuService.modifyMenuIsDelById(menuId, AppConstants.COMMON_BYTE_YES,loginUser.getUsername());
        return AjaxResultDTO.success();
    }

}