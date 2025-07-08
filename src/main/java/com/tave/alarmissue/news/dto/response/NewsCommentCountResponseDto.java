package com.tave.alarmissue.news.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Schema(description = "뉴스 상세 페이지별 댓글 개수 응답")
public class NewsCommentCountResponseDto {
    @Schema(description = "뉴스 ID", example = "1")
    private Long newsId;
//    @Schema(description = "댓글 ID", example = "1")
//    private Long commentId;
    @Schema(description = "댓글 개수", example = "15")
    private Long count;    //댓글 삭제했을 때 댓글 개수를 받아오기 위해.
}
