package com.tave.alarmissue.fcm.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class FcmNotificationResponse {
    private String messageId;
    private String fcmToken;
    private LocalDateTime sentAt;
}