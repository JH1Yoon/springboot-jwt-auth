package com.develop.springguard.domain.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "로그인 요청 데이터")
public class LoginRequest {
    @Schema(description = "사용자명")
    private String username;
    @Schema(description = "비밀번호")
    private String password;

    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
}