package com.tave.alarmissue.notification.dto.request;

import com.tave.alarmissue.notification.domain.enums.NotificationStatus;
import com.tave.alarmissue.notification.domain.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationHistoryRequest {
    private Long userId;
    private NotificationType notificationType;
    private String title;
    private String body;
    private NotificationStatus status;
    private String fcmMessageId;
    private String failureReason;
    private Long relatedEntityId;
}