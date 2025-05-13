package com.develop.springguard.domain.auth.service;

import com.develop.springguard.common.jwt.JwtUtil;
import com.develop.springguard.domain.auth.dto.request.LoginRequest;
import com.develop.springguard.domain.auth.dto.request.SignupRequest;
import com.develop.springguard.domain.auth.dto.response.AdminGrantResponse;
import com.develop.springguard.domain.auth.entity.User;
import com.develop.springguard.domain.auth.enums.UserRoleEnum;
import com.develop.springguard.domain.auth.repository.UserRepository;
import com.develop.springguard.exception.CustomException;
import com.develop.springguard.exception.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;


    private User adminUser;
    private User regularUser;
    private User targetUser;

    @BeforeEach
    void setUp() {
        adminUser = new User("admin", "encodedPassword", "Admin User", Set.of(UserRoleEnum.ADMIN));
        regularUser = new User("user", "encodedPassword", "Regular User", Set.of(UserRoleEnum.USER));
        targetUser = new User("targetUser", "encodedPassword", "Target User", Set.of(UserRoleEnum.USER));
        // 필요한 stubbing 설정만 진행
        lenient().when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        lenient().when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
    }

    @Test
    @DisplayName("회원가입 성공")
    void Signup_success() {
        // Given
        SignupRequest request = new SignupRequest("user1", "pass123", "nickname1");
        when(userRepository.findByUsername("user1")).thenReturn(Optional.empty());

        // When
        var response = authService.signupUser(request);

        // Then
        verify(userRepository).save(any(User.class));
        assertEquals("user1", response.getUsername());
    }
    
    @Test
    @DisplayName("회원가입 실패 - 이미 존재하는 유저")
    void Signup_fail_useralreadyexisted() {
        // Given
        User existingUser = new User("user1", "encodedPass", "nickname1", Set.of(UserRoleEnum.USER));
        when(userRepository.findByUsername("user1")).thenReturn(Optional.of(existingUser));
        SignupRequest request = new SignupRequest("user1", "pass123", "nickname1");

        // When & Then
        CustomException e = assertThrows(CustomException.class, () -> authService.signupUser(request));
        assertEquals(ErrorCode.USER_ALREADY_EXISTS, e.getErrorCode());
    }

    @Test
    @DisplayName("로그인 성공")
    void Login_success() {
        // Given
        String username = "user1";
        String password = "pass123";
        String encoded = passwordEncoder.encode(password);

        User user = new User(username, encoded, "nick", Set.of(UserRoleEnum.USER));
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(jwtUtil.createToken(username, UserRoleEnum.USER)).thenReturn("mock-token");

        // When
        var response = authService.login(new LoginRequest(username, password));

        // Then
        assertEquals("mock-token", response.getToken());
    }

    @Test
    @DisplayName("로그인 실패 - 잘못된 비밀번호")
    void Login_fail_invalidpassword() {
        // Given
        String username = "user1";
        String wrongPassword = "wrong";
        String correctPassword = passwordEncoder.encode("correct");

        User user = new User(username, correctPassword, "nick", Set.of(UserRoleEnum.USER));
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        when(passwordEncoder.matches(wrongPassword, correctPassword)).thenReturn(false);

        // When
        CustomException e = assertThrows(CustomException.class,
                () -> authService.login(new LoginRequest(username, wrongPassword)));

        // Then
        assertEquals(ErrorCode.INVALID_CREDENTIALS, e.getErrorCode());
    }

    @Test
    @DisplayName("관리자 권한 부여 - 관리자 요청 시 정상 처리")
    void grantAdminRole_success() {
        // Given
        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(adminUser));

        Set<UserRoleEnum> targetUserRoles = new HashSet<>(Set.of(UserRoleEnum.USER));
        targetUser.setRoles(targetUserRoles);

        when(userRepository.findById(1L)).thenReturn(Optional.of(targetUser));

        // When
        AdminGrantResponse response = authService.grantAdminRole(1L, adminUser);

        // Then
        assertEquals("targetUser", response.getUsername());
        assertEquals("Target User", response.getNickname());
        assertEquals("ADMIN", response.getRoles().get(0).getRole());
    }

    @Test
    @DisplayName("관리자 권한 부여 - 일반 사용자 요청 시 권한 오류 발생")
    void grantAdminRole_fail_accessDenied() {
        // Given
        when(userRepository.findByUsername("user")).thenReturn(Optional.of(regularUser));

        // When & Then
        CustomException e = assertThrows(CustomException.class, () -> authService.grantAdminRole(1L, regularUser));
        assertEquals(ErrorCode.ADMIN_ACCESS_DENIED, e.getErrorCode());
    }

    @Test
    @DisplayName("관리자 권한 부여 - 존재하지 않는 사용자에게 권한 부여 시 오류 발생")
    void grantAdminRole_fail_userNotFound() {
        // Given
        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(adminUser));
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        CustomException e = assertThrows(CustomException.class, () -> authService.grantAdminRole(999L, adminUser));
        assertEquals(ErrorCode.USER_NOT_FOUND, e.getErrorCode());
    }

}