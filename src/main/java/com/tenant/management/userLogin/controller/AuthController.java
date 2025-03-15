package com.tenant.management.userLogin.controller;

import com.tenant.management.userLogin.requestdtos.LoginRequest;
import com.tenant.management.userLogin.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        String token = authService.login(loginRequest);
        if (token != null && !token.isEmpty()) {
            return ResponseEntity.ok(token); // Return JWT token
        } else {
            return ResponseEntity.status(401).body("Invalid username or password");
        }
    }
}