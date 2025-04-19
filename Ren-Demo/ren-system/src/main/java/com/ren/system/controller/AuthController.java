package com.ren.system.controller;

import com.ren.system.properties.TokenProperties;
import com.ren.system.entity.BaseResponse;
import com.ren.system.entity.LoginRequest;
import com.ren.system.service.UserService;
import io.jsonwebtoken.Claims;
import com.ren.system.entity.LoginResponse;
import com.ren.system.entity.User;
import com.ren.system.security.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;  // 可存任意对象（需序列化）
    @Autowired
    private TokenProperties tokenProperties;
    @Autowired
    private UserService userService;

    /*
     * 自定义登录接口
     * @param request
     * @return org.springframework.http.ResponseEntity<?>
     * @author admin
     * @date 2025/04/17 15:59
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        try {
            //验证用户名密码
            //参数：SpringSecurity的认证管理器
            //如果认证成功,发挥返回Authentication，供用户使用
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
            //将认证信息存储到线程绑定的SecurityContext中，供Spring Security后续通过SecurityContextHolder获取当前用户身份，用于：鉴权（如@PreAuthorize("hasRole('ADMIN')")），获取用户信息（如@AuthenticationPrincipal User user）
            SecurityContextHolder.getContext().setAuthentication(authentication);
            //获取用户身份信息
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            // 生成双Token
            String accessToken = jwtUtils.createAccessToken(userDetails);
            String refreshToken = jwtUtils.createRefreshToken(userDetails);

            //获取自己创建的User并返回
            User user = userService.getUserByUsername(userDetails.getUsername());
            //密码不可以返回
            user.setPassword("");

            return ResponseEntity.ok()
                    .header(tokenProperties.getHeader(), "Bearer " + accessToken)
                    .body(new LoginResponse(200, "登陆成功", accessToken, refreshToken, user));
        } catch (BadCredentialsException e) {
            // 密码错误

            return ResponseEntity.status(401).body(new BaseResponse(401, "用户名或密码错误"));
        } catch (AuthenticationException e) {
            // 其他认证异常（如用户被锁定）
            return ResponseEntity.status(401).body(new BaseResponse(401, "用户名或密码错误"));
        }
    }

    /*
     * 自定义登出接口
     * @param user
     * @return org.springframework.http.ResponseEntity<?>
     * @author admin
     * @date 2025/04/17 19:42
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@AuthenticationPrincipal User user) {
        // 手动清理Security上下文
        SecurityContextHolder.clearContext();

        // 删除Redis中的RefreshToken
        jwtUtils.deleteRefreshToken(user.getId());
        // 将现在的这个AccessToken加入黑名单，防止退出后还能登录
        redisTemplate.opsForValue().set(
                "blacklist:" + user.getId(),
                "logged_out",
                tokenProperties.getExpireTime(),
                TimeUnit.SECONDS);

        return ResponseEntity.ok(new BaseResponse(200, "退出成功"));
    }

    /*
     * 自定义刷新token接口
     * @param refreshToken
     * @return org.springframework.http.ResponseEntity<?>
     * @author admin
     * @date 2025/04/17 19:42
     */
    @PostMapping("/refreshToken")
    public ResponseEntity<?> refreshToken(@RequestHeader("X-Refresh-Token") String refreshToken) {
        //从请求头中获取RefreshToken，只有RefreshToken有效才允许刷新Token，并验证refreshToken是否有效
        if (!jwtUtils.validateRefreshToken(refreshToken)) {
            return ResponseEntity.status(401).body(new BaseResponse(401, "用户名或密码错误"));
        }

        //将refreshToken解析为Claims
        Claims claims = jwtUtils.parseRefreshToken(refreshToken);
        //从Claims中获取UserDetails
        UserDetails userDetails = userService.getUserById(claims.get("user_id", Long.class));

        // 生成新的双Token
        String newAccessToken = jwtUtils.createAccessToken(userDetails);
        String newRefreshToken = jwtUtils.createRefreshToken(userDetails);

        // 返回双Token
        return ResponseEntity.ok()
                .header(tokenProperties.getHeader(), "Bearer " + newAccessToken)
                .body(Collections.singletonMap("refreshToken", newRefreshToken));
    }

}