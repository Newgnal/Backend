package com.tave.alarmissue.like.dto.response;

import com.tave.alarmissue.like.domain.LikeType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
@AllArgsConstructor
public class LikeResponseDto {
    private Long likeId;
    private boolean liked;
    private LikeType likeType;
}
