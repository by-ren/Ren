package com.ren.system.entity;

import lombok.Data;

import java.util.List;

@Data
public class LoginRequest {

    private String username;
    private String password;

}