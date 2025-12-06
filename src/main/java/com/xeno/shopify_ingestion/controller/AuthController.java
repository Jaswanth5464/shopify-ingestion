package com.xeno.shopify_ingestion.controller;

import com.xeno.shopify_ingestion.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    // Signup
    @PostMapping("/signup")
    public Map<String, Object> signup(@RequestBody Map<String, String> request) {
        return authService.signup(
                request.get("email"),
                request.get("password"),
                request.get("fullName"),
                request.get("storeName"),
                request.get("storeUrl"),
                request.get("accessToken")
        );
    }

    // Login
    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> request) {
        return authService.login(
                request.get("email"),
                request.get("password")
        );
    }
}