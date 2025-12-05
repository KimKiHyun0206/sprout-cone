package com.sproutcone.dto.internal.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KakaoLogoutResponseDto {
    private Long id; // 로그아웃된 사용자의 카카오 고유 ID
}