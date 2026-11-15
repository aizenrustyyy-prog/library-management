package com.aizenrusty.library_management.auth.controller;

import com.aizenrusty.library_management.auth.dto.LoginRequestDto;
import com.aizenrusty.library_management.auth.dto.LoginResponseDto;
import com.aizenrusty.library_management.auth.service.AuthService;
import com.aizenrusty.library_management.common.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseDto>> login(@Valid @RequestBody LoginRequestDto dto) {
        return ResponseEntity.ok(ApiResponse.success(authService.login(dto)));
    }
}
