package com.ren.framework.security.filter;

import com.ren.common.core.domain.bo.LoginUser;
import com.ren.common.manager.redis.RedisOperateManager;
import com.ren.framework.security.utils.JwtUtils;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private RedisOperateManager redisOperateManager;
    /*
     * 过滤器，在请求到达Controller之前执行，用于验证Token是否有效，并设置认证信息到SecurityContext中
     * @param request
     * @param response
     * @param chain
     * @author ren
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

            // 验证 accessToken 是否正常
            int validationResult = jwtUtils.validateAccessToken(accessToken);
            if (validationResult != 200) {
                // Token无效（如过期、黑名单、篡改），抛错，被下方的AuthenticationException-catch块捕获并抛出
                throw new BadCredentialsException("Token 无效");
            }

            LoginUser loginUser = jwtUtils.getLoginUserByToken((byte)1,accessToken);
            //验证refreshToken是否存在（主要用于强退功能）
            String refreshTokenKey = redisOperateManager.getRefreshTokenKey(loginUser.getUserId(),request);
            if (!redisOperateManager.hasKey(refreshTokenKey)) { // refreshToken是否存在
                // 账号被强退，需重新登录
                throw new BadCredentialsException("refreshToken 无效");
            }

            if (jwtUtils.shouldRefreshToken(accessToken)) { // accessToken需要续期,重新生成Authentication对象，并存储
                //获取新的AccessToken
                String newAccessToken = jwtUtils.saveNewAuthenticationAndReturnAccessToken((byte)1,accessToken);
                response.setHeader("X-Access-Token", "Bearer " + newAccessToken);
            }else{ // accessToken无需续期,存储原Authentication，用于登陆验证
                Authentication authentication = jwtUtils.getAuthenticationByAccessToken(accessToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            //通过验证，并且token续期成功，放行请求
            chain.doFilter(request, response);
        } catch (AuthenticationException ex) {
            log.debug("Token为空或无效", ex);
            // 收集AuthenticationException及其子类的所有异常，统一抛出，触发AuthenticationEntryPoint，最终都会被MyAuthenticationEntryPoint处理
            throw ex;
        } catch (ExpiredJwtException ex) {
            log.debug("Token已过期", ex);
            // JWT过期异常转换，在这里也抛出BadCredentialsException，方便触发AuthenticationEntryPoint，最终都会被MyAuthenticationEntryPoint处理
            throw new BadCredentialsException("Token 已过期", ex);
        } catch (JwtException | IllegalArgumentException ex) {
            // 签名无效、格式错误 → 返回 422
            log.debug("AccessToken验证失败: {}", ex.getMessage(), ex);
            throw new BadCredentialsException("AccessToken验证失败", ex);
        } catch (Exception ex) {
            log.debug("Token解析失败", ex);
            // 其他异常转换为认证异常，在这里也抛出BadCredentialsException，方便触发AuthenticationEntryPoint，最终都会被MyAuthenticationEntryPoint处理
            throw new BadCredentialsException("Token 解析失败", ex);
        }

    }

    /*
     * 不需要鉴权的接口
     * @param uri
     * @return boolean
     * @author ren
     * @date 2025/04/18 17:08
     */
    private boolean isOpenEndpoint(String uri) {
        AntPathMatcher matcher = new AntPathMatcher();
        return matcher.match("/auth/login", uri) ||
                matcher.match("/auth/refreshToken", uri)||

                // 静态资源 (使用通配符匹配)
                matcher.match("/", uri) ||
                matcher.match("/*.html", uri) ||
                matcher.match("/**/*.html", uri) ||  // 多层HTML
                matcher.match("/**/*.css", uri) ||   // 多层CSS
                matcher.match("/**/*.js", uri) ||    // 多层JS
                matcher.match("/profile/**", uri) || // 所有profile路径

                // 其他开放端点
                matcher.match("/swagger-ui/**", uri) ||
                matcher.match("/*/api-docs/**", uri) ||
                matcher.match("/swagger-resources/**", uri) ||
                matcher.match("/webjars/**", uri) ||
                matcher.match("/druid/**", uri) ||

                // 特殊匹配：OPTIONS 方法通配
                (uri.equals("OPTIONS") && matcher.match("/**", uri));
    }


}