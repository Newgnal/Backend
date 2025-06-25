package com.tave.alarmissue.fcm.controller;

import com.tave.alarmissue.fcm.dto.request.FcmSendReqeust;
import com.tave.alarmissue.fcm.dto.request.FcmTokenRequest;
import com.tave.alarmissue.fcm.service.FcmService;
import com.tave.alarmissue.fcm.service.FcmTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class FcmController {

    private final FcmService fcmService;
    private final FcmTokenService fcmTokenService;


    // 알림 전송
    @PostMapping("/api/notification/token")
    public void sendNotificationByToken(@RequestBody FcmSendReqeust fcmSendDto) {
        fcmService.sendPushNotification(fcmSendDto);
    }

    // 토큰 등록/갱신
    @PostMapping
    public ResponseEntity<Void> registerToken(@RequestBody FcmTokenRequest dto) {
        fcmTokenService.registerToken(dto.getUserId(), dto.getFcmToken());
        return ResponseEntity.ok().build();
    }

    // 토큰 삭제
    @DeleteMapping
    public ResponseEntity<Void> deleteToken(@RequestParam String fcmToken) {
        fcmTokenService.deleteToken(fcmToken);
        return ResponseEntity.ok().build();
    }
}