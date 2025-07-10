package com.tave.alarmissue.newsroom.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KeywordOrderRequest {
    @Schema(description = "순서대로 정렬된 키워드 ID 목록", example = "[3, 1, 2]")
    private List<Long> keywordIds;
}
