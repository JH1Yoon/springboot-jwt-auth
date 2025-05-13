package com.develop.springguard.domain.auth.dto.request;

import lombok.Getter;

@Getter
public class SignupRequest {
    private String username; // 사용자명
    private String password; // 비밀번호
    private String nickname; // 닉네임

    public SignupRequest(String username, String password, String nickname) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
    }
}