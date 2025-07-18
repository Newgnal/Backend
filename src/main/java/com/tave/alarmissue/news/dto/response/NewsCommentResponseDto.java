package com.tave.alarmissue.news.dto.response;

import com.tave.alarmissue.news.domain.enums.NewsVoteType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class NewsCommentResponseDto {
    @Schema(description = "댓글 ID")
    private Long commentId;
    private String comment;
    private NewsVoteType voteType;
    private String nickName;
    private LocalDateTime createdAt;
    private String timeAgo;
    @Schema(description = "좋아요 개수")
    private Long likeCount;
    @Schema(description = "현재 사용자가 좋아요를 눌렀는지 여부")
    private Boolean isLiked;
}
