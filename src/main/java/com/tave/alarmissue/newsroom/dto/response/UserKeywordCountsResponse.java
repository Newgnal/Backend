package com.tave.alarmissue.newsroom.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "사용자 키워드별 뉴스 개수 목록 응답")
public class UserKeywordCountsResponse {
    @Schema(description = "키워드별 뉴스 개수 목록")
    private List<KeywordCountResponse> keywordCounts;
}
