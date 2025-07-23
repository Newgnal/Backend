package com.tave.alarmissue.notification.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "방해금지 시간 설정 요청")
public class DoNotDisturbRequest {
    @Schema(description = "방해금지 모드 활성화 여부")
    private Boolean enabled;

    @Schema(description = "방해금지 시작 시간", example = "22:00")
    private LocalTime startTime;

    @Schema(description = "방해금지 종료 시간", example = "07:00")
    private LocalTime endTime;
}