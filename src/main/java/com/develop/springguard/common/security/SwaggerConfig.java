package com.develop.springguard.common.security;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("SpringGuard API 문서")
                        .version("1.0.0")
                        .description("로그인 / 회원가입 / 관리자 권한 API 문서")
                        .contact(new Contact()
                                .name("SpringGuard")
                                .email("wlgus4589@naver.com")
                                .url("https://github.com/JH1Yoon/springboot-jwt-auth")))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
    }
}