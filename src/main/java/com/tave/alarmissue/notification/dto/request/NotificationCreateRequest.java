package com.tave.alarmissue.notification.dto.request;

import com.tave.alarmissue.notification.domain.enums.NotificationType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@Schema(description = "알림 구성 요청")
public class NotificationCreateRequest {

    @Schema(description = "FCM 토큰")
    @NotBlank(message = "FCM 토큰은 필수입니다.")
    private String fcmToken;

    @Schema(description = "알림 타입")
    @NotNull(message = "알림 타입은 필수입니다.")
    private NotificationType notificationType;

    @Schema(description = "관련 엔티티 ID")
    private Long relatedEntityId;

    @Schema(description = "템플릿 매개변수")
    private Map<String, Object> templateParams;

    public NotificationCreateRequest(String fcmToken, NotificationType notificationType,
                                     Long relatedEntityId, Map<String, Object> templateParams) {
        this.fcmToken = fcmToken;
        this.notificationType = notificationType;
        this.relatedEntityId = relatedEntityId;
        this.templateParams = templateParams != null ? templateParams : new HashMap<>();
    }
}

