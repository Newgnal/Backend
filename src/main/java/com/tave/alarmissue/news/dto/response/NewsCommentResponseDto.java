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
    private Long newsId;
    @Schema(description = "댓글 ID")
    private Long commentId;
    @Schema(description = "댓글 개수")
    private Long count;
    private String comment;
    private NewsVoteType voteType;
    private String nickName;
    private LocalDateTime createdAt;
    private String timeAgo;
//    private LocalDateTime updatedAt;
}
