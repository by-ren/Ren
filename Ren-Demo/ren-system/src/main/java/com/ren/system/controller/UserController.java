package com.ren.system.controller;

import cn.hutool.core.convert.Convert;
import com.ren.system.common.constant.AppConstants;
import com.ren.system.entity.AjaxResult;
import com.ren.system.entity.User;
import com.ren.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    /*
     * 用户信息
     * @AuthenticationPrincipal User loginUser可以直接从SpringSecurity中获取到当前的用户信息
     * @param user
     * @return org.springframework.http.ResponseEntity<?>
     * @author admin
     * @date 2025/04/17 19:42
     */
    @GetMapping("/info")
    public AjaxResult getUserInfo(@AuthenticationPrincipal User loginUser) {
        //获取用户信息，并返回
        AjaxResult ajax = AjaxResult.success();
        ajax.put("user", loginUser);
        return ajax;
    }

    /*
     * 用户列表
     * @return com.ren.system.entity.AjaxResult
     * @author admin
     * @date 2025/04/26 15:55
     */
    @PostMapping("/list")
    public AjaxResult getUserList(@RequestBody(required = false) Map<String,Object> paramMap) {
        List<User> userList = userService.listUserByParam(paramMap);
        return AjaxResult.success().put("userList",userList);
    }

    /*
     * 添加用户
     * @param loginUser
     * @param addUser
     * @return com.ren.system.entity.AjaxResult
     * @author admin
     * @date 2025/05/04 16:26
     */
    @PostMapping("/add")
    public AjaxResult addUser(@AuthenticationPrincipal User loginUser,@RequestBody(required = false) User addUser) {
        userService.addUser(addUser,loginUser.getUsername());
        return AjaxResult.success();
    }

    /*
     * 编辑用户
     * @param loginUser
     * @param modifyUser
     * @return com.ren.system.entity.AjaxResult
     * @author admin
     * @date 2025/05/04 16:08
     */
    @PostMapping("/modify")
    public AjaxResult modifyUser(@AuthenticationPrincipal User loginUser,@RequestBody(required = false) User modifyUser) {
        userService.modifyUser(modifyUser,loginUser.getUsername());
        return AjaxResult.success();
    }

    /*
     * 删除用户
     * @param user
     * @param userId
     * @return com.ren.system.entity.AjaxResult
     * @author admin
     * @date 2025/05/04 15:27
     */
    @DeleteMapping("/delete")
    public AjaxResult userDelete(@AuthenticationPrincipal User loginUser,long userId) {
        userService.modifyUserIsDelById(userId, AppConstants.COMMON_BYTE_YES,loginUser.getUsername());
        return AjaxResult.success();
    }

    /*
     * 重置密码
     * @param user
     * @param paramMap
     * @return com.ren.system.entity.AjaxResult
     * @author admin
     * @date 2025/05/04 14:33
     */
    @PostMapping("/resetPassword")
    public AjaxResult resetPassword(@AuthenticationPrincipal User loginUser,@RequestBody(required = false) Map<String,Object> paramMap) {
        userService.resetPassword(Convert.toLong(paramMap.get("userId")),Convert.toStr(paramMap.get("password")),loginUser.getUsername());
        return AjaxResult.success();
    }

}