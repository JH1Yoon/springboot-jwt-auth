package com.develop.springguard.domain.auth.controller;

import com.develop.springguard.domain.auth.dto.request.LoginRequest;
import com.develop.springguard.domain.auth.dto.request.SignupRequest;
import com.develop.springguard.domain.auth.dto.response.AdminGrantResponse;
import com.develop.springguard.domain.auth.dto.response.LoginResponse;
import com.develop.springguard.domain.auth.dto.response.SignupResponse;
import com.develop.springguard.domain.auth.entity.User;
import com.develop.springguard.domain.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // 일반 사용자 회원가입 API
    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> signupUser(@RequestBody SignupRequest signupRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.signupUser(signupRequest));
    }

    // 관리자 회원가입 API
    @PostMapping("/signup/admin")
    public ResponseEntity<SignupResponse> signupAdmin(@RequestBody SignupRequest signupRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.signupAdmin(signupRequest));
    }

    // 로그인 API
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    // 관리자 권한 부여 API
    @PatchMapping("/admin/users/{userId}/roles")
    public ResponseEntity<AdminGrantResponse> grantAdminRole(@PathVariable Long userId, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(authService.grantAdminRole(userId, user));
    }
}