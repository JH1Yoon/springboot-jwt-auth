package com.develop.springguard.domain.auth.service;

import com.develop.springguard.common.jwt.JwtUtil;
import com.develop.springguard.domain.auth.dto.request.SignupRequest;
import com.develop.springguard.domain.auth.dto.request.LoginRequest;
import com.develop.springguard.domain.auth.dto.response.AdminGrantResponse;
import com.develop.springguard.domain.auth.dto.response.LoginResponse;
import com.develop.springguard.domain.auth.dto.response.SignupResponse;
import com.develop.springguard.domain.auth.entity.User;
import com.develop.springguard.domain.auth.enums.UserRoleEnum;
import com.develop.springguard.domain.auth.repository.UserRepository;
import com.develop.springguard.exception.CustomException;
import com.develop.springguard.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    // 일반 사용자 회원가입
    public SignupResponse signupUser(SignupRequest signupRequest) {
        if (userRepository.findByUsername(signupRequest.getUsername()).isPresent()) {
            throw new CustomException(ErrorCode.USER_ALREADY_EXISTS);
        }

        String encodedPassword = passwordEncoder.encode(signupRequest.getPassword());

        Set<UserRoleEnum> roles = new HashSet<>();
        roles.add(UserRoleEnum.USER);

        User newUser = new User(
                signupRequest.getUsername(),
                encodedPassword,
                signupRequest.getNickname(),
                roles);

        userRepository.save(newUser);

        return new SignupResponse(newUser.getUsername(), newUser.getNickname(), List.of(new SignupResponse.RoleDto("USER")));
    }

    // 관리자 회원가입
    public SignupResponse signupAdmin(SignupRequest signupRequest) {
        if (userRepository.findByUsername(signupRequest.getUsername()).isPresent()) {
            throw new CustomException(ErrorCode.USER_ALREADY_EXISTS);
        }

        String encodedPassword = passwordEncoder.encode(signupRequest.getPassword());

        Set<UserRoleEnum> roles = new HashSet<>();
        roles.add(UserRoleEnum.ADMIN);

        User newAdmin = new User(
                signupRequest.getUsername(),
                encodedPassword,
                signupRequest.getNickname(),
                roles);

        userRepository.save(newAdmin);

        return new SignupResponse(newAdmin.getUsername(), newAdmin.getNickname(), List.of(new SignupResponse.RoleDto("ADMIN")));
    }

    // 로그인
    public LoginResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_CREDENTIALS));

        checkPassword(loginRequest.getPassword(), user.getPassword());

        UserRoleEnum role = user.getRoles().contains(UserRoleEnum.ADMIN) ? UserRoleEnum.ADMIN : UserRoleEnum.USER;

        return new LoginResponse(jwtUtil.createToken(user.getUsername(), role));
    }

    // 관리자 권한 부여
    public AdminGrantResponse grantAdminRole(Long userId, User user) {
        User currentUser = userRepository.findByUsername(user.getUsername())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (!currentUser.getRoles().contains(UserRoleEnum.ADMIN)) {
            throw new CustomException(ErrorCode.ADMIN_ACCESS_DENIED);
        }

        User targetUser = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (!targetUser.getRoles().contains(UserRoleEnum.ADMIN)) {
            targetUser.getRoles().add(UserRoleEnum.ADMIN);
        }

        return new AdminGrantResponse(targetUser.getUsername(), targetUser.getNickname(), List.of(new AdminGrantResponse.RoleDto("ADMIN")));
    }

    // 비밀번호 확인
    private void checkPassword(String requestPassword, String userPassword) {
        if (!passwordEncoder.matches(requestPassword, userPassword)) {
            throw new CustomException(ErrorCode.INVALID_CREDENTIALS);
        }
    }
}