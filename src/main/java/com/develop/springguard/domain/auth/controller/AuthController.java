package com.develop.springguard.domain.auth.controller;

import com.develop.springguard.domain.auth.dto.request.LoginRequest;
import com.develop.springguard.domain.auth.dto.request.SignupRequest;
import com.develop.springguard.domain.auth.dto.response.AdminGrantResponse;
import com.develop.springguard.domain.auth.dto.response.LoginResponse;
import com.develop.springguard.domain.auth.dto.response.SignupResponse;
import com.develop.springguard.domain.auth.entity.User;
import com.develop.springguard.domain.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "로그인 및 회원가입 API", description = "로그인 및 회원가입, 관리자 권한 부여 API를 제공합니다.")
public class AuthController {

    private final AuthService authService;

    // 일반 사용자 회원가입 API
    @PostMapping("/signup")
    @Operation(summary = "일반 사용자 회원가입", description = "일반 사용자를 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "회원가입 성공",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\n  \"username\": \"JIN HO\",\n  \"nickname\": \"Mentos\",\n  \"roles\": [{ \"role\": \"USER\" }] }")
                    )),
            @ApiResponse(responseCode = "409", description = "이미 가입된 사용자",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\n  \"error\": {\n    \"code\": \"USER_ALREADY_EXISTS\",\n    \"message\": \"이미 가입된 사용자입니다.\" }\n}")
                    ))
    })
    public ResponseEntity<SignupResponse> signupUser(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = SignupRequest.class),
                            examples = @ExampleObject(value = "{\n  \"username\": \"JIN HO\",\n  \"password\": \"12341234\",\n  \"nickname\": \"Mentos\"\n}")
                    )
            )
            @RequestBody SignupRequest signupRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.signupUser(signupRequest));
    }

    // 관리자 회원가입 API
    @PostMapping("/signup/admin")
    @Operation(summary = "관리자 회원가입", description = "관리자를 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "회원가입 성공",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\n  \"username\": \"adminUser\",\n  \"nickname\": \"AdminNickname\",\n  \"roles\": [{ \"role\": \"ADMIN\" }] }")
                    )),
            @ApiResponse(responseCode = "409", description = "이미 가입된 사용자",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\n  \"error\": {\n    \"code\": \"USER_ALREADY_EXISTS\",\n    \"message\": \"이미 가입된 사용자입니다.\" }\n}")
                    ))
    })
    public ResponseEntity<SignupResponse> signupAdmin(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = SignupRequest.class),
                            examples = @ExampleObject(value = "{\n  \"username\": \"adminUser\",\n  \"password\": \"adminPass123\",\n  \"nickname\": \"AdminNickname\"\n}")
                    )
            )
            @RequestBody SignupRequest signupRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.signupAdmin(signupRequest));
    }

    // 로그인 API
    @PostMapping("/login")
    @Operation(summary = "로그인", description = "사용자가 로그인합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = LoginResponse.class),
                            examples = @ExampleObject(value = "{\n  \"token\": \"eKDIkdfjoakIdkfjpekdkcjdkoIOdjOKJDFOlLDKFJKL\"\n}")
                    )),
            @ApiResponse(responseCode = "401", description = "로그인 실패 (잘못된 계정 정보)",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = "{\n  \"error\": {\n    \"code\": \"INVALID_CREDENTIALS\",\n    \"message\": \"아이디 또는 비밀번호가 올바르지 않습니다.\"\n  }\n}")
                    ))
    })
    public ResponseEntity<LoginResponse> login(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = LoginRequest.class),
                            examples = @ExampleObject(value = "{\n  \"username\": \"adminUser\",\n  \"password\": \"adminPass123\"\n}")
                    )
            )
            @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    // 관리자 권한 부여 API
    @Operation(
            summary = "관리자 권한 부여",
            description = "사용자에게 관리자 권한을 부여합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "관리자 권한 부여 성공",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(name = "Success", value = "{\n" + "  \"username\": \"JIN HO\",\n" + "  \"nickname\": \"Mentos\",\n" + "  \"roles\": [\n" + "    { \"role\": \"Admin\" }\n" + "  ]\n" + "}"))
            ),
            @ApiResponse(responseCode = "403", description = "권한 부족 (접근 제한)",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(name = "Access Denied", value = "{\n" + "  \"error\": {\n" + "    \"code\": \"ADMIN_ACCESS_DENIED\",\n" + "    \"message\": \"관리자 권한이 필요한 요청입니다. 접근 권한이 없습니다.\"\n" + "  }\n" + "}"))
            )
    })
    @PatchMapping("/admin/users/{userId}/roles")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminGrantResponse> grantAdminRole(
            @Parameter(description = "관리자 권한을 부여할 사용자 ID", example = "1")
            @PathVariable Long userId,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(authService.grantAdminRole(userId, user));
    }
}