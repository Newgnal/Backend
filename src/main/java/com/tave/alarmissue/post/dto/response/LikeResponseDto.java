package com.tave.alarmissue.post.dto.response;

import com.tave.alarmissue.post.domain.LikeType;
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
