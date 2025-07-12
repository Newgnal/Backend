package com.tave.alarmissue.news.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Schema(description = "뉴스 댓글 목록 응답")
public class NewsCommentListResponseDto {

    @Schema(description = "뉴스 ID")
    private Long newsId;

    @Schema(description = "총 댓글 개수")
    private Long totalCount;

    @Schema(description = "댓글 목록")
    private List<NewsCommentResponseDto> comments;
}
