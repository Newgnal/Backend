package com.tave.alarmissue.news.dto.request;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
//댓글 생성 요청 DTO
@Schema(description = "댓글 생성 요청")
public class NewsCommentCreateRequestDto {
    @Schema(description = "생성된 댓글 정보")
    private String commentContent;
}
