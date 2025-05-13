package com.develop.springguard.domain.auth.controller;

import com.develop.springguard.domain.auth.dto.request.LoginRequest;
import com.develop.springguard.domain.auth.dto.request.SignupRequest;
import com.develop.springguard.domain.auth.dto.response.AdminGrantResponse;
import com.develop.springguard.domain.auth.dto.response.LoginResponse;
import com.develop.springguard.domain.auth.dto.response.SignupResponse;
import com.develop.springguard.domain.auth.entity.User;
import com.develop.springguard.domain.auth.enums.UserRoleEnum;
import com.develop.springguard.domain.auth.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthControllerTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private AuthService authService;

    private MockMvc mockMvc;

    private SignupRequest signupRequest;
    private LoginRequest loginRequest;
    private User mockUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();

        signupRequest = new SignupRequest("testUser", "password123", "nickname");
        loginRequest = new LoginRequest("testUser", "password123");

        mockUser = new User(1L, "testUser", "password123", "nickname", Set.of(UserRoleEnum.USER));
    }

    @Test
    @DisplayName("일반 유저 로그인 성공")
    void signupUser_success() throws Exception {
        // given
        SignupResponse mockResponse = new SignupResponse("testUser", "nickname", List.of(new SignupResponse.RoleDto("ROLE_USER")));
        when(authService.signupUser(any(SignupRequest.class))).thenReturn(mockResponse);

        // when
        mockMvc.perform(post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"testUser\", \"password\": \"password123\", \"nickname\": \"nickname\"}"))
                // then
                .andExpect(status().isCreated());  // 201 상태 코드만 검증
    }

    @Test
    @DisplayName("관리자 로그인 성공")
    void signupAdmin_success() throws Exception {
        // given
        SignupResponse mockResponse = new SignupResponse("testAdmin", "adminNickname", List.of(new SignupResponse.RoleDto("ROLE_ADMIN")));
        when(authService.signupAdmin(any(SignupRequest.class))).thenReturn(mockResponse);

        // when
        mockMvc.perform(post("/signup/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"testAdmin\", \"password\": \"password123\", \"nickname\": \"adminNickname\"}"))
                // then
                .andExpect(status().isCreated());  // 201 상태 코드만 검증

        verify(authService, times(1)).signupAdmin(any(SignupRequest.class));
    }

    @Test
    @DisplayName("로그인 성공")
    void login_success() throws Exception {
        // given
        LoginResponse mockResponse = new LoginResponse("mockToken");
        when(authService.login(any(LoginRequest.class))).thenReturn(mockResponse);

        // when
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"testUser\", \"password\": \"password123\"}"))
                // then
                .andExpect(status().isOk())  // 200 상태 코드 예상
                .andExpect(jsonPath("$.token").value("mockToken"));

        verify(authService, times(1)).login(any(LoginRequest.class));
    }

    @Test
    @DisplayName("권한 관리자로 변경 성공")
    void grantAdminRole_success() throws Exception {
        // given
        AdminGrantResponse mockResponse = new AdminGrantResponse(
                "JIN HO",
                "Mentos",
                List.of(new AdminGrantResponse.RoleDto("Admin"))
        );
        when(authService.grantAdminRole(eq(1L), any(User.class))).thenReturn(mockResponse);

        // when
        mockMvc.perform(patch("/admin/users/{userId}/roles", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"JIN HO\", \"password\": \"password123\"}"))
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("JIN HO"))
                .andExpect(jsonPath("$.nickname").value("Mentos"))
                .andExpect(jsonPath("$.roles[0].role").value("Admin"));

        verify(authService).grantAdminRole(eq(1L), any(User.class));
    }
}