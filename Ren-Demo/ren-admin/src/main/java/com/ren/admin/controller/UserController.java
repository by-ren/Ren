package com.ren.admin.controller;

import cn.hutool.core.convert.Convert;
import com.ren.common.constant.AppConstants;
import com.ren.common.core.dto.AjaxResultDTO;
import com.ren.common.core.entity.User;
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
    public AjaxResultDTO getUserInfo(@AuthenticationPrincipal User loginUser) {
        //获取用户信息，并返回
        AjaxResultDTO ajax = AjaxResultDTO.success();
        ajax.put("user", loginUser);
        return ajax;
    }

    /*
     * 用户列表
     * @return com.ren.admin.common.dto.AjaxResult
     * @author admin
     * @date 2025/04/26 15:55
     */
    @PostMapping("/list")
    public AjaxResultDTO listUserByPage(@RequestBody(required = false) Map<String,Object> paramMap) {
        List<User> userList = userService.listUserByParam(paramMap);
        return AjaxResultDTO.success().put("userList",userList);
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
    public AjaxResultDTO userDelete(@AuthenticationPrincipal User loginUser, long userId) {
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