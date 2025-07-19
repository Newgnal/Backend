package com.tave.alarmissue.news.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "댓글 수정 요청")
public class NewsCommentUpdateRequest {
    @Schema(description = "수정할 댓글 내용", example = "수정된 댓글 내용입니다.")
    private String comment;
}
