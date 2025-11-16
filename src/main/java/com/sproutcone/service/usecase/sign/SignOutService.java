package com.sproutcone.service.usecase.sign;

import com.sproutcone.domain.KakaoToken;
import com.sproutcone.domain.Runner;
import com.sproutcone.repository.RunnerJpaRepository;
import com.sproutcone.service.webclient.kakao.KakaoApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SignOutService {
    private final KakaoApiService kakaoApiService;
    private final RunnerJpaRepository runnerRepository;

    @Transactional
    public boolean signOut(Authentication authentication) {
        Long runnerId = Long.parseLong(authentication.getName());

        // 2. Runner 및 KakaoToken 조회
        Runner runner = runnerRepository.findById(runnerId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        KakaoToken kakaoToken = runner.getKakaoToken();

        // 3. 카카오 토큰이 이미 없으면(로그아웃 상태) 즉시 성공 처리
        if (kakaoToken == null) {
            return true;
        }

        // 4. (핵심) KakaoApiService를 통해 카카오 토큰 만료
        try {
            kakaoApiService.kakaoLogout(kakaoToken.getAccessToken());
        } catch (Exception e) {
            // 카카오 서버에서 이미 만료되었거나 유효하지 않은 토큰이라도
            // 우리 서버에서는 로그아웃을 계속 진행해야 합니다.
            log.warn("카카오 토큰 만료 처리 중 오류 발생 (로그아웃 계속 진행)", e);
            return false;
        }

        // 5. (핵심) 우리 DB에서 카카오 토큰 삭제 (Runner의 FK를 null로 설정)
        // orphanRemoval = true로 설정했기 때문에,
        // runner.updateKakaoToken(null) 후 runner를 save하면
        // KakaoToken 테이블의 데이터가 자동으로 DELETE 됩니다.
        runner.updateKakaoToken(null);
        runnerRepository.save(runner);
        return true;
    }
}