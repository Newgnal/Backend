package com.tave.alarmissue.news.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tave.alarmissue.news.domain.enums.NewsVoteType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

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
    @Schema(description = "부모 댓글 ID (답글인 경우만)")
    private Long parentId;    //부모댓글 id, null이면 원댓글, 값이 있으면 답글

    @Schema(description = "답글 목록")
    @JsonInclude(JsonInclude.Include.NON_NULL) // null인 경우 JSON에서 제외
    private List<NewsCommentResponseDto> replies;    //답글 리스트

    @Schema(description = "답글 개수")
    @JsonInclude(JsonInclude.Include.NON_NULL) // null인 경우 JSON에서 제외
    private Integer replyCount;
    @Schema(description = "좋아요 개수")
    private Long likeCount;
    @Schema(description = "현재 사용자가 좋아요를 눌렀는지 여부")
    private Boolean isLiked;
}
