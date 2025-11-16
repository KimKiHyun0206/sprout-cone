package com.sproutcone.service.data;

import com.sproutcone.repository.KakaoTokenJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KakaoTokenDataService {
    private final KakaoTokenJpaRepository kakaoTokenJpaRepository;

}