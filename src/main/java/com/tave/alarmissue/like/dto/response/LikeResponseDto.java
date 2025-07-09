package com.tave.alarmissue.like.dto.response;

import com.tave.alarmissue.like.domain.LikeType;
import com.tave.alarmissue.like.domain.Liked;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
@AllArgsConstructor
public class LikeResponseDto {
    private Long likeId;
    private Liked liked;
    private LikeType likeType;
}
