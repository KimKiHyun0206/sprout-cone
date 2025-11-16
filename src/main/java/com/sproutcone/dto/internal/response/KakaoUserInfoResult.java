package com.sproutcone.dto.internal.response;

import lombok.Builder;

import java.util.Date;
import java.util.HashMap;

@Builder
public record KakaoUserInfoResult(
        Long id,
        Boolean hasSignedUp,
        Date connectedAt,
        Date synchedAt,
        HashMap<String, String> properties,
        KakaoAccountResult kakaoAccountResult,
        PartnerResult partner
) {
}