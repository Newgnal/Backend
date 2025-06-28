package com.tave.alarmissue.fcm.dto.request;

import lombok.Getter;

@Getter
public class FcmSendReqeust {
    private String token;
    private String title;
    private String body;
}
