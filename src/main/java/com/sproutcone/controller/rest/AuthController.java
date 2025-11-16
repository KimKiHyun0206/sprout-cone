package com.sproutcone.controller.rest;

import com.sproutcone.domain.KakaoToken;
import com.sproutcone.domain.Runner;
import com.sproutcone.dto.internal.response.KakaoTokenResponseDto;
import com.sproutcone.dto.internal.response.KakaoUserInfoResponseDto;
import com.sproutcone.repository.KakaoTokenJpaRepository;
import com.sproutcone.repository.RunnerJapRepository;
import com.sproutcone.service.jwt.JwtProvider;
import com.sproutcone.service.webclient.kakao.KakaoApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Collections;

// 예시: AuthController
@RestController
@RequestMapping("/auth/kakao")
@RequiredArgsConstructor
public class AuthController {

    private final KakaoApiService kakaoApiService;
    private final RunnerJapRepository runnerRepository;
    private final KakaoTokenJpaRepository kakaoTokenRepository;
    private final JwtProvider jwtProvider;

    // 1. 프론트엔드에서 인가 코드를 받아 이 API를 호출
    @GetMapping("/callback")
    public ResponseEntity<String> kakaoLogin(@RequestParam String code) {
        
        // 2. 카카오 토큰 받기 (WebClient 호출)
        KakaoTokenResponseDto tokenDto = kakaoApiService.getKakaoToken(code);

        // 3. 카카오 사용자 정보 받기 (WebClient 호출)
        KakaoUserInfoResponseDto userInfo = kakaoApiService.getKakaoUserInfo(tokenDto.getAccessToken());

        // 4. (비즈니스 로직) 사용자 정보로 Runner 가입 또는 조회
        Runner runner = runnerRepository.findByEmail(userInfo.getEmail())
                .orElseGet(() -> {
                    // 신규 회원이면 Runner 생성 (이름, 이메일, 임시 비밀번호 등)
                    Runner newRunner = new Runner(userInfo.getNickname(), userInfo.getEmail(), "tempPassword"); 
                    return runnerRepository.save(newRunner);
                });

        // 5. (비즈니스 로직) KakaoToken 엔티티 저장/업데이트
        KakaoToken kakaoToken = KakaoToken.toEntity(
                runner, 
                tokenDto.getAccessToken(), 
                tokenDto.getRefreshToken()
        );
        kakaoTokenRepository.save(kakaoToken);
        
        // 6. (비즈니스 로직) Runner 엔티티와 KakaoToken 엔티티 연관관계 설정
        // (이 부분은 엔티티 설계에 따라 다름. 이미 KakaoToken 생성자에 Runner가 포함됨)
        // ...
        
        // 7. 우리 서비스의 JWT(Access Token) 발급
        // (JwtProvider는 사용자 이름(email)과 권한을 기반으로 토큰을 생성해야 함)
        Authentication authentication = createAuthentication(userInfo.getEmail()); // (별도 구현 필요)
        String appAccessToken = jwtProvider.generateToken(authentication);

        // 8. 클라이언트에 우리 서비스의 JWT 반환
        return ResponseEntity.ok(appAccessToken);
    }

    /**
     * (신규 추가)
     * 인증된 사용자 이메일과 기본 권한을 기반으로 Spring Security의 Authentication 객체를 생성합니다.
     *
     * @param email 인증된 사용자의 이메일 (JWT의 Subject가 됨)
     * @return UsernamePasswordAuthenticationToken (Authentication 구현체)
     */
    private Authentication createAuthentication(String email) {
        // 프로토타입이므로, 카카오 로그인을 통해 인증된 모든 사용자는
        // "ROLE_USER" 권한을 가지도록 설정합니다.
        Collection<? extends GrantedAuthority> authorities =
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));

        // UsernamePasswordAuthenticationToken(principal, credentials, authorities)
        // 1. principal: 사용자 식별자 (여기서는 email)
        // 2. credentials: 자격 증명 (비밀번호 - JWT 기반이므로 null 처리)
        // 3. authorities: 권한 목록
        return new UsernamePasswordAuthenticationToken(email, null, authorities);
    }
}