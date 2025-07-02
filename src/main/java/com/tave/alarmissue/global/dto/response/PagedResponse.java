package com.tave.alarmissue.global.dto.response;

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
@Schema(description = "커서 기반 페이지네이션 응답")
public class PagedResponse<T> {
    @Schema(description = "데이터 목록")
    private List<T> content;

    @Schema(description = "다음 페이지 존재 여부", example = "true")
    private boolean hasNext;

    @Schema(description = "다음 요청에 사용할 마지막 ID", example = "90")
    private Long nextLastId;

    @Schema(description = "현재 페이지 크기", example = "10")
    private int size;

    @Schema(description = "전체 데이터 개수", example = "150")
    private Long totalCount;
}
