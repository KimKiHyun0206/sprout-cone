package com.sproutcone.service.jwt;

import com.sproutcone.config.properties.JWTProperties;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
public class JwtFilter extends OncePerRequestFilter {

    public final String AUTHORIZATION_HEADER ;
    public final String BEARER_PREFIX;

    private final JwtProvider jwtProvider;

    public JwtFilter(JwtProvider jwtProvider, JWTProperties jwtProperties) {
        this.jwtProvider = jwtProvider;
        this.AUTHORIZATION_HEADER = jwtProperties.accessTokenHeader();
        this.BEARER_PREFIX = jwtProperties.bearerHeader();
    }

    // 실제 필터링 로직
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 1. 요청(Request) 헤더에서 토큰을 추출합니다.
        String jwt = resolveToken(request);

        // 2. 토큰 유효성 검증
        if (StringUtils.hasText(jwt) && jwtProvider.validateToken(jwt)) {
            // 토큰이 유효하면 인증 정보(Authentication)를 가져옵니다.
            Authentication authentication = jwtProvider.getAuthentication(jwt);
            
            // SecurityContext에 인증 정보를 저장합니다.
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.debug("Security Context에 '{}' 인증 정보를 저장했습니다, uri: {}", authentication.getName(), request.getRequestURI());
        } else {
            log.debug("유효한 JWT 토큰이 없습니다, uri: {}", request.getRequestURI());
        }

        // 다음 필터로 요청을 전달합니다.
        filterChain.doFilter(request, response);
    }

    // 요청(Request) 헤더에서 "Bearer " 접두사를 제거하고 토큰 값만 추출합니다.
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7); // "Bearer " 7글자
        }
        return null;
    }
}