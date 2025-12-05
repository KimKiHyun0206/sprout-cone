package com.sproutcone.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import org.springframework.context.annotation.Configuration;

/**
 * Springdoc OpenAPI 3.0 설정
 * * @OpenAPIDefinition: API의 기본 정보 (제목, 버전, 설명)를 정의합니다.
 * @SecurityScheme: "bearerAuth"라는 이름으로 JWT Bearer 토큰 인증 스키마를 정의합니다.
 * - type = HTTP: HTTP 기반 인증
 * - scheme = "bearer": Bearer 토큰 스키마
 * - bearerFormat = "JWT": 토큰 형식
 * @SecurityRequirement: API 문서 전역에 "bearerAuth" 보안 요구사항을 적용합니다.
 * 이를 통해 모든 API 엔드포인트에 자물쇠 아이콘이 표시되고,
 * "Authorize" 버튼을 통해 입력된 JWT가 모든 요청 헤더에 포함됩니다.
 */
@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "SproutCone API 명세서", // API 제목
        version = "v1.0",              // API 버전
        description = "SproutCone 프로젝트의 API 명세서입니다." // API 설명
    ),
    security = { @SecurityRequirement(name = "bearerAuth") } // 전역 보안 요구사항 설정
)
@SecurityScheme(
    name = "bearerAuth", // SecurityRequirement의 name과 일치해야 함
    type = SecuritySchemeType.HTTP,
    scheme = "bearer",
    bearerFormat = "JWT"
)
public class OpenApiConfig {
    // 이 클래스는 설정 어노테이션을 제공하는 역할만 하므로 내부는 비어있습니다.
}