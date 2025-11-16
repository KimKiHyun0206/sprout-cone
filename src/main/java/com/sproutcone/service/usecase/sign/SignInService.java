package com.sproutcone.service.usecase.sign;

import com.sproutcone.service.data.KakaoTokenDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignInService {
    private final KakaoTokenDataService kakaoTokenDataService;
}