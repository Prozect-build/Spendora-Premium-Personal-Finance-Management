package com.spendora.spendora_backend.controller;

import com.spendora.spendora_backend.dto.ApiResponse;
import com.spendora.spendora_backend.dto.AuthRequest;
import com.spendora.spendora_backend.dto.RegisterRequest;
import com.spendora.spendora_backend.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Map<String, String>>> register(@Valid @RequestBody RegisterRequest request) {
        String token = authService.register(request);
        return ResponseEntity.ok(ApiResponse.success(Map.of("token", token), "Registration successful"));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Map<String, String>>> login(@Valid @RequestBody AuthRequest request) {
        String token = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success(Map.of("token", token), "Login successful"));
    }
}
