package com.ren.system.controller;

import com.ren.system.entity.AjaxResult;
import com.ren.system.entity.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

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

}
