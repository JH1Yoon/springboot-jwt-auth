package com.develop.springguard.domain.auth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "로그인 응답 데이터")
public class LoginResponse {
    @Schema(description = "JWT 토큰")
    private String token; // 토큰

    public LoginResponse(String token) {
        this.token = token;
    }
}