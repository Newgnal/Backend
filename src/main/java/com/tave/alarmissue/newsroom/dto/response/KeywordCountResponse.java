package com.tave.alarmissue.newsroom.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "키워드별 뉴스 개수 응답")
public class KeywordCountResponse {
    @Schema(description = "키워드 ID", example = "1")
    private Long keywordId;

    @Schema(description = "키워드 이름", example = "인공지능")
    private String keywordName;

    @Schema(description = "뉴스 개수", example = "15")
    private Long count;
}
