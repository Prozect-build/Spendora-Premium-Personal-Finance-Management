package com.spendora.spendora_backend.controller;

import com.spendora.spendora_backend.dto.ApiResponse;
import com.spendora.spendora_backend.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<User>> getCurrentUser(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(ApiResponse.success(user, "User profile fetched successfully"));
    }
}
