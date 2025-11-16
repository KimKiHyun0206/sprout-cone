package com.sproutcone.service.usecase.sign;

import com.sproutcone.domain.KakaoToken;
import com.sproutcone.domain.Runner;
import com.sproutcone.dto.internal.response.KakaoTokenResponseDto;
import com.sproutcone.dto.internal.response.KakaoUserInfoResponseDto;
import com.sproutcone.repository.KakaoTokenJpaRepository;
import com.sproutcone.repository.RunnerJpaRepository;
import com.sproutcone.service.jwt.JwtProvider;
import com.sproutcone.service.utils.AuthenticationUtils;
import com.sproutcone.service.webclient.kakao.KakaoApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignInService {
    private final KakaoApiService kakaoApiService;
    private final KakaoTokenJpaRepository kakaoTokenJpaRepository;
    private final RunnerJpaRepository runnerJpaRepository;
    private final JwtProvider jwtProvider;

    public String signIn(String code) {
        // 2. 카카오 토큰 받기 (WebClient 호출)
        KakaoTokenResponseDto tokenDto = kakaoApiService.getKakaoToken(code);

        // 3. 카카오 사용자 정보 받기 (WebClient 호출)
        KakaoUserInfoResponseDto userInfo = kakaoApiService.getKakaoUserInfo(tokenDto.getAccessToken());

        // 4. (비즈니스 로직) 사용자 정보로 Runner 가입 또는 조회
        Runner runner = runnerJpaRepository.findByEmail(userInfo.getEmail())
                .orElseGet(() -> {
                    // 신규 회원이면 Runner 생성 (이름, 이메일, 임시 비밀번호 등)
                    Runner newRunner = new Runner(userInfo.getNickname(), userInfo.getEmail(), "tempPassword");
                    return runnerJpaRepository.save(newRunner);
                });

        // 5. (비즈니스 로직) KakaoToken 엔티티 저장/업데이트
        KakaoToken kakaoToken = KakaoToken.toEntity(
                runner,
                tokenDto.getAccessToken(),
                tokenDto.getRefreshToken()
        );
        kakaoTokenJpaRepository.save(kakaoToken);

        // 6. [수정] Runner에 KakaoToken을 설정하고 Runner를 업데이트 (관계 주인 저장)
        runner.updateKakaoToken(kakaoToken); // 2. Runner 객체에 KakaoToken 설정
        runnerJpaRepository.save(runner);       // 3. Runner 저장 (UPDATE 쿼리 발생)

        // 7. 우리 서비스의 JWT(Access Token) 발급
        // (JwtProvider는 사용자 이름(email)과 권한을 기반으로 토큰을 생성해야 함)
        Authentication authentication = AuthenticationUtils.createAuthentication(runner.getId()); // (별도 구현 필요)
        return  jwtProvider.generateToken(authentication);
    }
}