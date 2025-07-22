package com.tave.alarmissue.news.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
@Schema(description = "좋아요 상태 조회용 응답")
public class NewsCommentLikeStatusResponse {
    @Schema(description = "댓글 ID")
    private Long commentId;

    @Schema(description = "사용자 ID")
    private Long userId;

    @Schema(description = "좋아요 상태(true: 좋아요, false: 좋아요 안함)")
    private Boolean isLiked;

    @Schema(description = "총 좋아요 개수")
    private Long likeCount;

}
