package com.develop.springguard.domain.auth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@Schema(description = "회원가입 응답 데이터")
public class SignupResponse {
    @Schema(description = "사용자 이름")
    private String username;

    @Schema(description = "닉네임")
    private String nickname;

    @Schema(description = "역할 목록")
    private List<RoleDto> roles;

    @Getter
    @AllArgsConstructor
    @Schema(description = "역할 DTO")
    public static class RoleDto {
        @Schema(description = "역할")
        private String role;
    }

    public SignupResponse(String username, String nickname, List<RoleDto> roles) {
        this.username = username;
        this.nickname = nickname;
        this.roles = roles;
    }
}
