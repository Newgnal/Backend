package com.tave.alarmissue.global.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "페이지네이션 요청")
public class PagenationRequest {
    @Schema(description = "마지막 항목 ID (무한 스크롤용)", example = "100")
    private Long lastId;

    @Schema(description = "페이지 크기", example = "10", minimum = "1", maximum = "50")
    @Min(value = 1, message = "페이지 크기는 1 이상이어야 합니다.")
    @Max(value = 50, message = "페이지 크기는 50 이하여야 합니다.")
    @Builder.Default
    private Integer size = 10;
}