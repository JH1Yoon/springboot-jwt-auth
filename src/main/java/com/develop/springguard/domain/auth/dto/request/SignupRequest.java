package com.develop.springguard.domain.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "회원가입 요청 데이터")
public class SignupRequest {

    @Schema(description = "사용자명")
    private String username;
    @Schema(description = "비밀번호")
    private String password;
    @Schema(description = "닉네임")
    private String nickname;

    public SignupRequest(String username, String password, String nickname) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
    }
}