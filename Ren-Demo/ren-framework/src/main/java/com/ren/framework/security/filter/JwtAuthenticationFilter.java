package com.ren.framework.security.filter;

import com.ren.framework.properties.TokenProperties;
import com.ren.framework.security.utils.JwtUtils;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private TokenProperties tokenProperties;

    /*
     * 过滤器，在请求到达Controller之前执行，用于验证Token是否有效，并设置认证信息到SecurityContext中
     * @param request
     * @param response
     * @param chain
     * @author admin
     * @date 2025/04/17 21:23
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {

        String requestURI = request.getRequestURI();

        // 跳过不需要 Token 的接口
        if (isOpenEndpoint(requestURI)) {
            chain.doFilter(request, response);
            return;
        }

        try {
            // 从请求头中获取 accessToken
            String accessToken = jwtUtils.getAccessToken(request);

            if (!StringUtils.hasText(accessToken)) {
                // AuthenticationCredentialsNotFoundException（认证凭证未找到异常）（状态码应该是401）、BadCredentialsException（凭证无效）（状态码应该是401）都是AuthenticationException（认证异常）的子类，只不过是不同的错误类型
                // Token为空，抛错，被下方的AuthenticationException-catch块捕获并抛出
                throw new AuthenticationCredentialsNotFoundException("未提供 Token");
            }

            // 验证 Token 并处理不同状态
            int validationResult = jwtUtils.validateAccessToken(accessToken);
            if (validationResult != 200) {
                // Token无效（如过期、黑名单、篡改），抛错，被下方的AuthenticationException-catch块捕获并抛出
                throw new BadCredentialsException("Token 无效");
            }

            // 从accessToken中解析出Authentication
            // Authentication：SpringSecurity的认证信息对象
            // 将认证信息存储到线程绑定的SecurityContext中
            // Spring Security后续通过SecurityContextHolder获取当前用户身份，用于：鉴权（如@PreAuthorize("hasRole('ADMIN')")），获取用户信息（如@AuthenticationPrincipal User user）
            Authentication authentication = jwtUtils.getAuthentication(accessToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 检查是否需要刷新Token
            if (jwtUtils.shouldRefreshToken(accessToken)) {
                //生成新的AccessToken并设置到响应头中
                String newToken = jwtUtils.createAccessToken((UserDetails) authentication.getPrincipal());
                response.setHeader("X-Access-Token", "Bearer " + newToken);
            }
            //通过验证，并且token续期成功，放行请求
            chain.doFilter(request, response);
        } catch (AuthenticationException ex) {
            // 收集AuthenticationException及其子类的所有异常，统一抛出，触发AuthenticationEntryPoint，最终都会被MyAuthenticationEntryPoint处理
            throw ex;
        } catch (ExpiredJwtException ex) {
            // JWT过期异常转换，在这里也抛出BadCredentialsException，方便触发AuthenticationEntryPoint，最终都会被MyAuthenticationEntryPoint处理
            throw new BadCredentialsException("Token 已过期", ex);
        } catch (Exception ex) {
            // 其他异常转换为认证异常，在这里也抛出BadCredentialsException，方便触发AuthenticationEntryPoint，最终都会被MyAuthenticationEntryPoint处理
            throw new BadCredentialsException("Token 解析失败", ex);
        }

    }

    /*
     * 不需要鉴权的接口
     * @param uri
     * @return boolean
     * @author admin
     * @date 2025/04/18 17:08
     */
    private boolean isOpenEndpoint(String uri) {
        AntPathMatcher matcher = new AntPathMatcher();
        return matcher.match("/auth/login", uri) ||
                matcher.match("/auth/refreshToken", uri);
    }


}