package com.tenant.management.userLogin.requestdtos;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}