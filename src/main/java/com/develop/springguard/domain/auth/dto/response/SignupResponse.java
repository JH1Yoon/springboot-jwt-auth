package com.develop.springguard.domain.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
public class SignupResponse {
    private String username; // 사용자명
    private String nickname; // 닉네임
    private List<RoleDto> roles;    // 역할

    @Getter
    @AllArgsConstructor
    public static class RoleDto {
        private String role;
    }

    public SignupResponse(String username, String nickname, List<RoleDto> roles) {
        this.username = username;
        this.nickname = nickname;
        this.roles = roles;
    }
}
