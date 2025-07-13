package com.tave.alarmissue.post.dto.response;

import com.tave.alarmissue.post.domain.enums.TargetType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
@AllArgsConstructor
public class LikeResponse {
    private Long likeId;
    private boolean liked;
    private TargetType targetType;
}
