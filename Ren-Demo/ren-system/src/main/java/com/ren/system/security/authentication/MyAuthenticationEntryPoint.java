package com.ren.system.security.authentication;

import com.alibaba.fastjson2.JSON;
import com.ren.system.entity.BaseResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class MyAuthenticationEntryPoint implements AuthenticationEntryPoint {

    /*
     * 认证失败统一处理程序
     * @param request
     * @param response
     * @param authException
     * @author admin
     * @date 2025/04/18 20:34
     */
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        //获取错误信息
        String localizedMessage = authException.getLocalizedMessage();
        //设置状态码和返回信息
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        //统一返回401（未提供有效身份凭证（需认证））
        BaseResponse errorResponse = new BaseResponse(401,localizedMessage);
        response.getWriter().write(JSON.toJSONString(errorResponse));
    }
}