package com.ren.framework.security.config;

import com.ren.framework.security.filter.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // 替换旧版@EnableGlobalMethodSecurity，表示允许使用注解控制权限
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    /*
     * 将AuthenticationManager 交给Spring管理，用于登录方法调用认证
     * @param authConfig
     * @return org.springframework.security.authentication.AuthenticationManager
     * @author admin
     * @date 2025/04/17 21:30
     */
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    /*
     * 配置过滤器链
     * @param http
     * @return org.springframework.security.web.SecurityFilterChain
     * @author admin
     * @date 2025/04/17 21:30
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // 禁用CSRF（API服务标准配置）
        http.csrf(csrf -> csrf.disable());

        // 允许跨域
        // 配置具体的 CORS 规则
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()));

        // 配置会话管理为无状态，告诉 Spring Security 不要用 Session 存储用户状态，所有认证信息通过 Token 传递
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.authorizeHttpRequests(auth -> auth
                // 允许 OPTIONS 方法通过
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                // 请求授权配置（放行登录和刷新token接口）
                .requestMatchers("/auth/login", "/auth/refreshToken").permitAll()
                // 除上面外的所有请求全部需要鉴权认证
                .anyRequest().authenticated()
        );


        // 添加JWT过滤器（这里使用的是Before，所以这个过滤器会在UsernamePasswordAuthenticationFilter之前执行）
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        // 彻底关闭表单登录（禁用默认接口）
        http.formLogin(form -> form.disable());
        // 自定义异常处理，返回给前端一个登录失败的Json字符串（避免默认登录页的跳转）
        http.exceptionHandling(exception  -> {
            exception.authenticationEntryPoint(new MyAuthenticationEntryPoint());//请求未认证的接口
        });

        //禁用默认的 HTTP Basic 认证
        http.httpBasic(basic -> basic.disable());

        //禁用默认登出接口（登出接口没必要直接放行，还是让他鉴权比较好）
        http.logout(logout -> logout.disable()); //禁用默认登出逻辑

        return http.build();
    }


    /*
     * 自定义 CORS 配置源
     * @author admin
     * @date 2025/04/19 18:24
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        // 允许所有来源（生产环境不要用）
        config.setAllowedOriginPatterns(Arrays.asList("*")); // 比 setAllowedOrigins 更灵活
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(Arrays.asList("*")); // 允许所有请求头
        config.setAllowCredentials(false); // 如果前端需要带凭证（如 cookies），改为 true

        // 暴露自定义响应头（如 X-Access-Token）,如果后面请求中用到了自定义相应头，那么这里一定要设置，不然即使设置了跨域，前端还是获取不到响应头
        config.addExposedHeader("X-Access-Token");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    /*
     * 配置密码编码器（固定死使用BCryptPasswordEncoder密码格式，数据库密码不可以有前缀）
     * @return org.springframework.security.crypto.password.PasswordEncoder
     * @author admin
     * @date 2025/04/17 21:40
     */
    /*@Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }*/

    /*
     * 配置密码编码器（多编码器模式，数据库密码一定要有前缀）
     * @return org.springframework.security.crypto.password.PasswordEncoder
     * @author admin
     * @date 2025/04/22 21:52
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}