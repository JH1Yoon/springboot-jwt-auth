package com.develop.springguard.domain.auth.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginRequest {
    private String username;  // 사용자명
    private String password;  // 비밀번호

    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
}