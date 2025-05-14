package com.develop.springguard.exception;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "에러 응답")
public class ErrorResponse {

    @Schema(description = "에러 코드", example = "USER_ALREADY_EXISTS")
    private String code;

    @Schema(description = "에러 메시지", example = "이미 가입된 사용자입니다.")
    private String message;

    // Getters and Setters
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}