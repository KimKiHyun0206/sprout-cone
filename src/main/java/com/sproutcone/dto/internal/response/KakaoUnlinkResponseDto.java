package com.sproutcone.dto.internal.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KakaoUnlinkResponseDto {
    private Long id; // 연결 끊기 된 사용자의 카카오 고유 ID
}