package com.develop.springguard.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    USER_ALREADY_EXISTS("USER_ALREADY_EXISTS", "이미 가입된 사용자입니다.", HttpStatus.CONFLICT),
    INVALID_CREDENTIALS("INVALID_CREDENTIALS", "아이디 또는 비밀번호가 올바르지 않습니다.", HttpStatus.UNAUTHORIZED),
    ADMIN_ACCESS_DENIED("ADMIN_ACCESS_DENIED", "관리자 권한이 필요한 요청입니다. 접근 권한이 없습니다.", HttpStatus.FORBIDDEN),
    USER_NOT_FOUND("USER_NOT_FOUND", "해당 사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    INVALID_TOKEN("INVALID_TOKEN", "유효하지 않은 인증 토큰입니다.", HttpStatus.UNAUTHORIZED),
    ACCESS_DENIED("ACCESS_DENIED", "접근 권한이 없습니다.", HttpStatus.FORBIDDEN);

    private final String code;
    private final String message;
    private final HttpStatus status;

    ErrorCode(String code, String message, HttpStatus status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getStatus() {
        return status;
    }
}