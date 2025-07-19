package com.tave.alarmissue.news.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "답글 생성 요청")
public class NewsReplyRequest {
    @Schema(description = "답글 내용", example = "답글입니다.")
    private String comment;
    @Schema(description = "부모 댓글 ID", example = "1")
    private Long parentId;   //부모댓글 id
}
