package com.tave.alarmissue.like.dto.request;

import com.tave.alarmissue.like.domain.LikeType;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LikeCreateRequestDto {
    private boolean liked;
    private LikeType likeType;
}
