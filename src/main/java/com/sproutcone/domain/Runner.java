package com.sproutcone.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Runner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "runner_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "runner", cascade = CascadeType.REMOVE)
    private List<RunningArt> runningArts;

    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "kakao_token_id")
    private KakaoToken kakaoToken;

    public Runner(String nickname, String email, String password){
        this.name = nickname;
        this.email = email;
        this.password = password;
    }

    public void updateKakaoToken(KakaoToken kakaoToken) {
        this.kakaoToken = kakaoToken;
    }
}