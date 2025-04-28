package com.ren.system.controller;

import com.ren.system.entity.AjaxResult;
import com.ren.system.entity.User;
import com.ren.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    /*
     * 用户信息
     * @AuthenticationPrincipal User user可以直接从SpringSecurity中获取到当前的用户信息
     * @param user
     * @return org.springframework.http.ResponseEntity<?>
     * @author admin
     * @date 2025/04/17 19:42
     */
    @GetMapping("/info")
    public AjaxResult getUserInfo(@AuthenticationPrincipal User user) {
        //获取用户信息，并返回
        AjaxResult ajax = AjaxResult.success();
        ajax.put("user", user);
        return ajax;
    }

    /*
     * 用户列表
     * @return com.ren.system.entity.AjaxResult
     * @author admin
     * @date 2025/04/26 15:55
     */
    @PostMapping("/list")
    public AjaxResult getUserList() {
        List<User> userList = userService.listUserByParam();
        return AjaxResult.success().put("userList",userList);
    }

}
