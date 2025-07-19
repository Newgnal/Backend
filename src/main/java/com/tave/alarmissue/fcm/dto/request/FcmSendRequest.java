package com.tave.alarmissue.fcm.dto.request;

import com.tave.alarmissue.notification.domain.enums.NotificationType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "FCM 푸시 알림 전송 요청")
public class FcmSendRequest {
    @Schema(description = "FCM 토큰")
    private String token;

    @Schema(description = "알림 제목")
    private String title;

    @Schema(description = "알림 내용")
    private String body;

    @Schema(description = "사용자 ID")
    private Long userId;

    @Schema(description = "알림 타입")
    private NotificationType notificationType;

    @Schema(description = "관련 엔티티 ID")
    private Long relatedEntityId;
}
