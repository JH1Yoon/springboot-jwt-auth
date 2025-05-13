package com.develop.springguard.domain.auth.dto.response;

import lombok.Getter;

@Getter
public class LoginResponse {
    private String token; // 토큰

    public LoginResponse(String token) {
        this.token = token;
    }
}