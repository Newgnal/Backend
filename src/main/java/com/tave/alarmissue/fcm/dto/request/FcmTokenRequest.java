package com.tave.alarmissue.fcm.dto.request;

import lombok.Getter;

@Getter
public class FcmTokenRequest {
    private Long userId;
    private String fcmToken;
}
