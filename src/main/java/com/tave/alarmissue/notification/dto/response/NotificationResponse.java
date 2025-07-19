package com.tave.alarmissue.notification.dto.response;

import com.tave.alarmissue.notification.domain.enums.NotificationType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "알림 응답")
public class NotificationResponse {
    @Schema(description = "알림 ID")
    private Long id;

    @Schema(description = "알림 타입")
    private NotificationType type;

    @Schema(description = "알림 타입명")
    private String typeName;

    @Schema(description = "알림 제목")
    private String title;

    @Schema(description = "알림 내용")
    private String body;

    @Schema(description = "읽음 여부")
    private Boolean isRead;

    @Schema(description = "시간 경과", example = "5분 전")
    private String timeAgo;

    @Schema(description = "생성 시간")
    private LocalDateTime createdAt;

    @Schema(description = "읽은 시간")
    private LocalDateTime readAt;

    @Schema(description = "관련 엔티티 ID")
    private Long relatedEntityId;

}