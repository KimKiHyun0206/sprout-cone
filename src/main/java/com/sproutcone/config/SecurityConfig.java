package com.sproutcone.config;

import com.sproutcone.service.jwt.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    // Swagger UI 접근을 위한 URL 목록
    private static final String[] SWAGGER_WHITELIST = {
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/swagger-resources/**",
            "/webjars/**"
    };


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // CSRF 비활성화

                // 세션을 사용하지 않음 (JWT 기반이므로 STATELESS)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth
                        // /api/login, /api/signup 등 인증 없이 허용할 엔드포인트

                        .requestMatchers("/api/auth/**", "/public/**",   "/", "/login",
    "/me", "/mypage",
    "/running-art-generate",
    "/running-art/**",
    "/css/**", "/js/**", "/images/**", "/favicon.ico",
    "/api/auth/**", "/public/**",
    "/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**", "/webjars/**").permitAll()

                        // Swagger UI 경로 허용
                        .requestMatchers(SWAGGER_WHITELIST).permitAll() //http://localhost:8080/swagger-ui.html
                        // 나머지 모든 요청은 인증 필요
                        .anyRequest().authenticated()
                );

        // JwtFilter를 UsernamePasswordAuthenticationFilter 앞에 추가
        // Spring Security의 기본 로그인 필터보다 먼저 JWT 필터를 실행하도록 설정
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}