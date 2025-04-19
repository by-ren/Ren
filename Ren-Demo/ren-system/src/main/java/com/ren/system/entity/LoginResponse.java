package com.ren.system.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private int code;
    private String message;
    private String token;
    private String refreshToken;
    private User user;

    // 构造方法（成功无 Token）
    public LoginResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

}