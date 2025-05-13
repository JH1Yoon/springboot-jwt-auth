package com.develop.springguard.common.security;

import com.develop.springguard.common.jwt.JwtUtil;
import com.develop.springguard.domain.auth.entity.User;
import com.develop.springguard.domain.auth.repository.UserRepository;
import com.develop.springguard.exception.CustomException;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String bearerToken = request.getHeader(JwtUtil.AUTHORIZATION_HEADER);
        String token = jwtUtil.substringToken(bearerToken);

        try {
            if (token != null) {
                jwtUtil.validateToken(token); // 예외 발생 가능

                Claims claims = jwtUtil.getUserInfoFromToken(token);
                String username = claims.getSubject();

                User user = userRepository.findByUsername(username)
                        .orElse(null);

                if (user != null) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            user, null,
                            user.getRoles().stream()
                                    .map(role -> (GrantedAuthority) () -> "ROLE_" + role.name())
                                    .toList()
                    );
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }

            filterChain.doFilter(request, response);
        } catch (CustomException e) {
            setErrorResponse(response, e);
        }
    }

    private void setErrorResponse(HttpServletResponse response, CustomException e) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json; charset=UTF-8");
        response.getWriter().write(String.format(
                "{\"error\": {\"code\": \"%s\", \"message\": \"%s\"}}",
                e.getErrorCode().name(),
                e.getMessage()
        ));
    }
}