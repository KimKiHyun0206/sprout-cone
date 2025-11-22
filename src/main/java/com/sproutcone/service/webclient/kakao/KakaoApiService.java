package com.sproutcone.service.webclient.kakao;

import com.sproutcone.config.properties.KakaoProperties;
import com.sproutcone.dto.internal.response.KakaoLogoutResponseDto;
import com.sproutcone.dto.internal.response.KakaoTokenResponseDto;
import com.sproutcone.dto.internal.response.KakaoUnlinkResponseDto;
import com.sproutcone.dto.internal.response.KakaoUserInfoResponseDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class KakaoApiService {
    private final WebClient kakaoAuthClient;
    private final WebClient kakaoApiClient;

    private final String CLIENT_ID;
    private final String REDIRECT_URI;

    public KakaoApiService(
            WebClient.Builder webClientBuilder,
            KakaoProperties kakaoProperties
    ) {

        // 1. 카카오 인증용 WebClient
        this.kakaoAuthClient = webClientBuilder
                .baseUrl(kakaoProperties.authApi())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .build();

        // 2. 카카오 API 호출용 WebClient
        this.kakaoApiClient = webClientBuilder
                .baseUrl(kakaoProperties.api())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .build();

        this.CLIENT_ID = kakaoProperties.clientId();
        this.REDIRECT_URI = kakaoProperties.redirectUri();
    }

    /**
     * 1. 인가 코드로 카카오 액세스 토큰 받기
     */
    public KakaoTokenResponseDto getKakaoToken(String code) {

        // WebClient는 비동기 Mono를 반환하므로 .block()을 통해 동기적으로 결과를 기다립니다.
        return kakaoAuthClient.post()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .path("/oauth/token") // 토큰 요청 엔드포인트
                        .queryParam("grant_type", "authorization_code")
                        .queryParam("client_id", CLIENT_ID)
                        .queryParam("code", code)
                        // build()를 사용하여 쿼리 파라미터를 안전하게 인코딩합니다.
                        .build())
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

    /**
     * 카카오 액세스 토큰을 만료시켜 카카오 서비스에서 로그아웃합니다.
     *
     * @param kakaoAccessToken 우리 DB에 저장되어 있던 카카오 액세스 토큰
     * @return 로그아웃된 사용자의 카카오 ID
     */
    public KakaoLogoutResponseDto kakaoLogout(String kakaoAccessToken) {
        String uri = "/v1/user/logout";

        return kakaoApiClient.post()
                .uri(uri)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + kakaoAccessToken)
                .retrieve()
                .bodyToMono(KakaoLogoutResponseDto.class)
                .block(); // MVC 환경이므로 동기 처리
    }

    /**
     * 카카오 앱과 사용자의 연결을 끊습니다 (회원 탈퇴).
     *
     * @param kakaoAccessToken 우리 DB에 저장되어 있던 카카오 액세스 토큰
     * @return 연결 끊기 된 사용자의 카카오 ID
     */
    public KakaoUnlinkResponseDto kakaoUnlink(String kakaoAccessToken) {
        String uri = "/v1/user/unlink";

        // 카카오 연결 끊기 API는 POST 방식입니다.
        return kakaoApiClient.post()
                .uri(uri)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + kakaoAccessToken)
                .retrieve()
                .bodyToMono(KakaoUnlinkResponseDto.class)
                .block(); // MVC 환경이므로 동기 처리
    }
}