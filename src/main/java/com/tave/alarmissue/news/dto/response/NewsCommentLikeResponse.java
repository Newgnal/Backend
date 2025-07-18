package com.tave.alarmissue.news.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
@Schema(description = "댓글/답글 좋아요 토글 응답")
public class NewsCommentLikeResponse {
    @Schema(description = "댓글 ID")
    private Long commentId;

    @Schema(description = "좋아요 상태(true: 좋아요, false: 좋아요 취소")
    private Boolean isLiked;

    @Schema(description = "총 좋아요 개수")
    private Long likeCount;



}
