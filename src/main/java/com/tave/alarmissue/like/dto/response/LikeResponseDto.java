package com.tave.alarmissue.like.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class LikeResponseDto {
    private Long likeId;
    private boolean liked;
}
