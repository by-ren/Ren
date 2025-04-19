package com.ren.system.entity;

import lombok.Data;

@Data
public class BaseResponse {
    private int code;
    private String message;

    // 构造方法（成功无 Token）
    public BaseResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

}