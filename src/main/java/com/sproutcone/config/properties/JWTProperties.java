package com.sproutcone.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

/**
 * JWT(JSON Web Token) 및 관련 보안 설정 값을 관리하는 타입-세이프(Type-Safe) 속성 클래스입니다.
 * <p>
 * 이 클래스는 {@code application.yml} (또는 {@code .properties}) 파일의
 * {@code "jwt"} 접두사를 가진 속성들을 이 레코드의 필드에 자동으로 바인딩합니다.
 *
 * @param accessTokenHeader           액세스 토큰을 담을 HTTP 헤더 이름 (예: "Authorization")
 * @param secret                      JWT 서명 및 검증용 Base64 인코딩 비밀 키
 * @param accessTokenValidityInHour   액세스 토큰의 유효 기간 (예: "30m", "1h")
 * @param authorityKey                토큰 클레임 내 Spring Security 권한을 식별하는 키 (예: "auth")
 * @param bearerHeader                액세스 토큰 전송 시 사용할 HTTP 인증 스키마 (일반적으로 "Bearer")
 */
@ConfigurationProperties(prefix = "jwt")
public record JWTProperties(

        /**
         * JWT 액세스 토큰이 HTTP 헤더에 담길 때 사용될 헤더 이름입니다.
         * (예: {@code Authorization})
         */
        String accessTokenHeader,

        /**
         * JWT 서명 및 검증에 사용되는 비밀 키(Secret Key)입니다.
         * <p>
         * <b>반드시 Base64 인코딩된</b> 문자열이어야 합니다.
         * HS256 알고리즘 사용 시, 256비트(32바이트) 이상의 강력한 키를 권장합니다.
         */
        String secret,

        /**
         * 액세스 토큰의 유효 기간(만료 기간)입니다.
         * <p>
         * Spring의 {@link Duration} 형식으로 파싱됩니다. (예: {@code "30m"}, {@code "1h"})
         */
        Duration accessTokenValidityInHour,

        /**
         * Spring Security 권한(Authority)을 식별하기 위해 토큰 클레임(Claim)에 사용될 키 이름입니다.
         * (예: {@code "auth"})
         */
        String authorityKey,

        /**
         * 액세스 토큰 전송 시 사용되는 HTTP 인증 스키마(Scheme)입니다.
         * <p>
         * 일반적으로 {@code "Bearer"}가 사용되며, 헤더 값은 {@code "Bearer {token}"} 형식이 됩니다.
         */
        String bearerHeader
) {
}