package com.ren.system.security.config;

import com.ren.system.security.authentication.MyAuthenticationEntryPoint;
import com.ren.system.security.filter.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // 替换旧版@EnableGlobalMethodSecurity
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

        // 允许 OPTIONS 方法通过
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
        );

        // 配置会话管理为无状态
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // 彻底关闭表单登录（禁用默认接口）
        http.formLogin(form -> form.disable());
        // 自定义异常处理，返回给前端一个登录失败的Json字符串（避免默认登录页的跳转）
        http.exceptionHandling(exception  -> {
            exception.authenticationEntryPoint(new MyAuthenticationEntryPoint());//请求未认证的接口
        });
        // 请求授权配置（放行登录和刷新token接口）
        http.authorizeHttpRequests(auth -> auth.requestMatchers("/auth/login", "/auth/refreshToken").permitAll().anyRequest().authenticated());

        //禁用默认登出接口（登出接口没必要直接放行，还是让他鉴权比较好）
        http.logout(logout -> logout.disable()); //禁用默认登出逻辑

        // 添加JWT过滤器（这里使用的是Before，所以这个过滤器会在UsernamePasswordAuthenticationFilter之前执行）
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

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

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    /*
     * 配置密码编码器
     * @return org.springframework.security.crypto.password.PasswordEncoder
     * @author admin
     * @date 2025/04/17 21:40
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}