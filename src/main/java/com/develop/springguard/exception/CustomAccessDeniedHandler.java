package com.develop.springguard.exception;

import jakarta.servlet.ServletException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(jakarta.servlet.http.HttpServletRequest request,
                       jakarta.servlet.http.HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {
        throw new CustomException(ErrorCode.ACCESS_DENIED);
    }
}