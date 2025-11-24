package com.sproutcone.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Entity
@NoArgsConstructor
public class KakaoToken {
    /**
     * 카카오 토큰 정보의 고유 식별자 (Primary Key)입니다.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "kakao_token_id")
    private Long id;

    /**
     * 토큰 소유자(사용자)의 고유 식별자입니다. (Foreign Key)
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "runner_id", nullable = false)
    private Runner runner;

    /**
     * 카카오 API 호출에 사용되는 액세스 토큰입니다.
     */
    @Column(nullable = false)
    private String accessToken;

    /**
     * 액세스 토큰을 갱신하는 데 사용되는 리프레시 토큰입니다.
     */
    @Column(nullable = false)
    private String refreshToken;

    /**
     * Builder 패턴을 사용하는 생성자입니다.
     * <p>Note: {@code id} 필드는 데이터베이스에 의해 자동 생성되므로 포함하지 않습니다.</p>
     */
    @Builder
    private KakaoToken(Runner runner, String accessToken, String refreshToken) {
        this.runner = runner;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    /**
     * 토큰 정보를 포함하는 새로운 {@code KakaoToken} 엔티티 인스턴스를 생성하는 정적 팩토리 메서드입니다.
     *
     * @param runner 토큰의 소유자인 사용자
     * @param accessToken 새로 발급받은 카카오 액세스 토큰입니다.
     * @param refreshToken 새로 발급받은 카카오 리프레시 토큰입니다.
     * @return 초기화된 {@code KakaoToken} 엔티티 인스턴스입니다.
     */
    public static KakaoToken toEntity(Runner runner, String accessToken, String refreshToken) {
        return KakaoToken.builder()
                .runner(runner)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}