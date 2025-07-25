package com.tave.alarmissue.notification.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "알림 타입 설정 요청")
public class NotificationTypeToggleRequest {
    @Schema(description = "활성화 여부")
    @NotNull
    private Boolean enabled;
}
