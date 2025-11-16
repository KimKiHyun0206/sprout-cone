package com.sproutcone.service.data;

import com.sproutcone.domain.KakaoToken;
import com.sproutcone.repository.KakaoTokenJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class KakaoTokenDataService {
    private final KakaoTokenJpaRepository kakaoTokenJpaRepository;

    @Transactional
    public KakaoToken save(KakaoToken kakaoToken) {
        return kakaoTokenJpaRepository.save(kakaoToken);
    }
}