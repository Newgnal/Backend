package com.tave.alarmissue.fcm.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class FcmTokenResponse {
    private String fcmToken;
    private Long userId;
    private LocalDateTime registeredAt;
}