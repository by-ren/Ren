package com.ren.framework.security.config;

import com.alibaba.fastjson2.JSON;
import com.ren.common.domain.dto.AjaxResultDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
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
                         AuthenticationException authException){
        log.info("进入SpringSecurity认证失败统一处理程序");
		try {
            //设置状态码和返回信息（本来这里应该设置为401，表示凭证失效异常）
            //但是如果这里设置了401，会被浏览器捕获到异常，并打印，不好看。所以这里我设置为200，并自定义一个返回信息，使用返回信息中的状态码来判断更好
            response.setStatus(200);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            //进入到这里之后，统一汇合为未提供有效身份验证，进行返回
			response.getWriter().write(JSON.toJSONString(AjaxResultDTO.error(401, "未提供有效身份凭证")));
		} catch (IOException e) {
            log.debug("SpringSecurity认证失败统一处理程序发生异常",e);
			throw new RuntimeException(e);
		}
	}
}