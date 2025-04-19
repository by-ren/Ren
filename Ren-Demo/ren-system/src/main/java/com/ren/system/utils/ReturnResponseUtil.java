package com.ren.system.utils;

import com.alibaba.fastjson2.JSON;
import com.ren.system.entity.BaseResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ReturnResponseUtil {

    /*
     * 统一发送JSON错误响应
     * @param response
     * @param code
     * @param message
     * @author admin
     * @date 2025/04/18 16:53
     */
    public static void sendErrorResponse(HttpServletResponse response, int code, String message) throws IOException {
        response.setStatus(code);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.getWriter().write(JSON.toJSONString(new BaseResponse(code, message)));
        response.getWriter().flush();
    }

}
