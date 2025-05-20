package com.ren.admin.controller;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson2.JSON;
import com.ren.common.domain.bo.LoginUser;
import com.ren.common.domain.dto.AjaxResultDTO;
import com.ren.common.domain.entity.User;
import com.ren.common.domain.enums.BusinessType;
import com.ren.common.interfaces.OperLogAnn;
import com.ren.common.utils.ip.IpUtils;
import com.ren.framework.properties.TokenProperties;
import com.ren.framework.security.config.AuthenticationContextHolder;
import com.ren.framework.security.utils.JwtUtils;
import com.ren.system.service.UserService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/auth")
@Slf4j
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
     * @param loginRequest
     * @return com.ren.admin.common.dto.AjaxResult
     * @author admin
     * @date 2025/04/24 10:28
     */
    @PostMapping("/login")
    public AjaxResultDTO login(@RequestBody Map<String,Object> paramMap, HttpServletRequest httpRequest) {
        try {
            //验证用户名密码
            //参数：SpringSecurity的认证管理器
            //如果认证成功,发挥返回Authentication，供用户使用
            Authentication authentication = null;

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(Convert.toStr(paramMap.get("username")), Convert.toStr(paramMap.get("password")));
            // 将用户名密码封装成一个springSecurity能够认识的对象，用于后面的认证流程
            AuthenticationContextHolder.setContext(authenticationToken);
            // 该方法会去调用UserDetailsServiceImpl.loadUserByUsername
            authentication = authenticationManager.authenticate(authenticationToken);

            // 存储到 SecurityContext,方便后面流程从SpringSecurity中直接获取用户信息
            SecurityContextHolder.getContext().setAuthentication(authentication);

            //获取用户身份信息
            LoginUser loginUser = (LoginUser) authentication.getPrincipal();

            // 生成双Token
            String accessToken = jwtUtils.createAccessToken(loginUser);
            String refreshToken = jwtUtils.createRefreshToken(loginUser);

            //更新用户最后登录时间
            User loginIpUser = userService.getUserByUsername(loginUser.getUsername());
            userService.modifyUserByLogin(loginIpUser.getUserId(),IpUtils.getIpAddr(),DateUtil.currentSeconds(),loginUser.getUsername());
            return AjaxResultDTO.success("登陆成功").put("accessToken",accessToken).put("refreshToken",refreshToken);
        } catch (BadCredentialsException e) {
            log.debug("登陆失败", e);
            // 密码错误
            return AjaxResultDTO.error(401, e.getMessage());
        } catch (AuthenticationException e) {
            log.debug("登陆失败", e);
            // 其他认证异常（如用户被锁定）
            return AjaxResultDTO.error(401, e.getMessage());
        }finally{
            AuthenticationContextHolder.clearContext();
        }
    }

    /*
     * 自动登录功能（没有什么其他操作，前台调用该接口实现自动登录，修改是否登录字段使用）
     * @param paramMap
     * @param httpRequest
     * @return com.ren.common.domain.dto.AjaxResultDTO
     * @author admin
     * @date 2025/05/16 17:21
     */
    @PostMapping("/auto/login")
    public AjaxResultDTO autoLogin() {
        return AjaxResultDTO.success();
    }

    /*
     * 自定义登出接口
     * @param user
     * @return com.ren.admin.common.dto.AjaxResult
     * @author admin
     * @date 2025/04/24 10:28
     */
    @PostMapping("/logout")
    public AjaxResultDTO logout(@AuthenticationPrincipal LoginUser loginUser, HttpServletRequest request) {
        // 手动清理Security上下文
        SecurityContextHolder.clearContext();

        // 删除Redis中的RefreshToken
        jwtUtils.deleteRefreshToken(loginUser.getUserId());

        // 将现在的这个AccessToken加入黑名单，防止退出后还能登录
        String accessToken = jwtUtils.getAccessToken(request);
        redisTemplate.opsForValue().set(
                "blacklist:" + accessToken,
                "logged_out",
                tokenProperties.getExpireTime(),//黑名单时长内也使用accesstoken的有效时长
                TimeUnit.SECONDS);

        return AjaxResultDTO.success("退出成功");
    }

    /*
     * 自定义刷新token接口
     * @param refreshToken
     * @return com.ren.admin.common.dto.AjaxResult
     * @author admin
     * @date 2025/04/24 10:28
     */
    @PostMapping("/refreshToken")
    public AjaxResultDTO refreshToken(@RequestHeader("X-Refresh-Token") String refreshToken) {
        //从请求头中获取RefreshToken，只有RefreshToken有效才允许刷新Token，并验证refreshToken是否有效
        if (!jwtUtils.validateRefreshToken(refreshToken)) {
            log.debug("refreshToken失效，请重新登录");
            return AjaxResultDTO.error(401, "refreshToken失效，请重新登录");
        }

        try {
            //将refreshToken解析为Claims
            Claims claims = jwtUtils.parseRefreshToken(refreshToken);
            String loginUserJson = claims.get("login_user", String.class);
            LoginUser loginUser = JSON.parseObject(loginUserJson, LoginUser.class);

            // 生成新的双Token
            String newAccessToken = jwtUtils.createAccessToken(loginUser);
            //refreshToken不重新生成，只要refreshToken过期，强制重新登陆一次
            //String newRefreshToken = jwtUtils.createRefreshToken(userDetails);

            // 返回双Token
            return AjaxResultDTO.success().put("accessToken",newAccessToken).put("refreshToken",refreshToken);
        }catch (Exception e){
            log.debug("refreshToken失效，请重新登录",e);
            return AjaxResultDTO.error(401, "refreshToken失效，请重新登录");
        }
    }

}