package com.sproutcone.dto.internal.response;

import lombok.Builder;

@Builder
public record ProfileResult(
        String nickName,
        String thumbnailImageUrl,
        String profileImageUrl,
        Boolean isDefaultImage,
        Boolean isDefaultNickName
) {}