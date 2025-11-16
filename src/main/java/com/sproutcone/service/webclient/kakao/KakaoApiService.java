package com.sproutcone.service.webclient.kakao;

import com.sproutcone.dto.internal.response.KakaoTokenResponseDto;
import com.sproutcone.dto.internal.response.KakaoUserInfoResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class KakaoApiService {
    private final WebClient kakaoAuthClient;
    private final WebClient kakaoApiClient;

    private final String clientId;
    private final String clientSecret;
    private final String redirectUri;

    public KakaoApiService(
            // WebClient.Builder를 주입받아 각각의 WebClient를 생성
            WebClient.Builder webClientBuilder,
            @Value("${kakao.auth-url}") String kakaoAuthUrl,
            @Value("${kakao.api-url}") String kakaoApiUrl,
            @Value("${kakao.client-id}") String clientId,
            @Value("${kakao.client-secret}") String clientSecret,
            @Value("${kakao.redirect-uri}") String redirectUri) {

        // 1. 카카오 인증용 WebClient
        this.kakaoAuthClient = webClientBuilder
                .baseUrl(kakaoAuthUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .build();

        // 2. 카카오 API 호출용 WebClient
        this.kakaoApiClient = webClientBuilder
                .baseUrl(kakaoApiUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .build();

        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
    }

    /**
     * 1. 인가 코드로 카카오 액세스 토큰 받기
     */
    public KakaoTokenResponseDto getKakaoToken(String code) {
        // Kakao 토큰 요청 API는 Content-Type이 application/x-www-form-urlencoded 입니다.
        String uri = "/oauth/token";

        // WebClient는 비동기 Mono를 반환하므로 .block()을 통해 동기적으로 결과를 기다립니다.
        return kakaoAuthClient.post()
                .uri(uri)
                .body(BodyInserters.fromFormData("grant_type", "authorization_code")
                        .with("client_id", clientId)
                        .with("client_secret", clientSecret)
                        .with("redirect_uri", redirectUri)
                        .with("code", code)
                )
                .retrieve() // 응답을 받음
                .bodyToMono(KakaoTokenResponseDto.class) // KakaoTokenResponseDto로 변환
                .block(); // 동기 실행 (MVC 환경)
    }

    /**
     * 2. 액세스 토큰으로 카카오 사용자 정보 받기
     */
    public KakaoUserInfoResponseDto getKakaoUserInfo(String accessToken) {
        String uri = "/v2/user/me";

        return kakaoApiClient.get()
                .uri(uri)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken) // Bearer 토큰 설정
                .retrieve()
                .bodyToMono(KakaoUserInfoResponseDto.class)
                .block();
    }
}