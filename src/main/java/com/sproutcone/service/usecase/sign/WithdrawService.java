package com.sproutcone.service.usecase.sign;

import com.sproutcone.domain.KakaoToken;
import com.sproutcone.domain.Runner;
import com.sproutcone.dto.internal.response.KakaoUnlinkResponseDto;
import com.sproutcone.repository.RunnerJpaRepository;
import com.sproutcone.service.webclient.kakao.KakaoApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class WithdrawService {
    private final KakaoApiService kakaoApiService;
    private final RunnerJpaRepository runnerJpaRepository;

    public boolean signOff(Authentication authentication) {
        // 1. JwtFilter를 통해 주입된 인증 정보에서 Runner ID 획득
        // (AuthController에서 AuthenticationUtils.createAuthentication(runner.getId())로 생성했으므로)
        Long runnerId = Long.parseLong(authentication.getName());

        // 2. Runner 엔티티 조회
        Runner runner = runnerJpaRepository.findById(runnerId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다. ID: " + runnerId));

        KakaoToken kakaoToken = runner.getKakaoToken();

        // 3. (선택적) 카카오와 연결 끊기 시도
        if (kakaoToken != null) {
            try {
                // 카카오 API 호출하여 연결 끊기
                KakaoUnlinkResponseDto kakaoUnlinkResponseDto = kakaoApiService.kakaoUnlink(kakaoToken.getAccessToken());
                log.info("카카오 연결 끊기 성공. Runner KAKAO ID: {}", kakaoUnlinkResponseDto);
            } catch (Exception e) {
                // 카카오 API 호출이 실패하더라도 (예: 토큰 만료, 카카오 서버 오류)
                // 우리 서비스의 회원 탈퇴는 계속 진행되어야 합니다.
                log.warn("카카오 연결 끊기 실패. Runner ID: {}. 오류: {}", runnerId, e.getMessage());
                return false;
            }
        }

        // 4. (핵심) 우리 서비스에서 Runner 엔티티 삭제
        // Runner 엔티티의 @OneToOne(cascade = REMOVE), @OneToMany(cascade = REMOVE) 설정 덕분에
        // runnerRepository.delete(runner)만 호출하면
        // 연관된 KakaoToken과 모든 RunningArt가 **자동으로 함께 삭제**됩니다.
        runnerJpaRepository.delete(runner);
        return true;
    }
}
