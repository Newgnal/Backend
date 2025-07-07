package com.tave.alarmissue.news.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Schema(description = "댓글 생성 응답")
public class NewsCommentCreateResponseDto {
    @Schema(description = "생성된 댓글 정보")
    private NewsCommentResponseDto comment;

    @Schema(description = "해당 뉴스의 총 댓글 개수")
    private Long totalCommentCount;
}
