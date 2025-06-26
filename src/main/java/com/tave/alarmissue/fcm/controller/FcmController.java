package com.tave.alarmissue.fcm.controller;

import com.tave.alarmissue.fcm.dto.request.FcmSendReqeust;
import com.tave.alarmissue.fcm.dto.request.FcmTokenRequest;
import com.tave.alarmissue.fcm.dto.response.FcmNotificationResponse;
import com.tave.alarmissue.fcm.dto.response.FcmTokenResponse;
import com.tave.alarmissue.fcm.service.FcmService;
import com.tave.alarmissue.fcm.service.FcmTokenService;
import com.tave.alarmissue.global.dto.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/fcm/v1")
@Tag(name = "FCM API", description = "Firebase Cloud Messaging 관련 API")
public class FcmController {

    private final FcmService fcmService;
    private final FcmTokenService fcmTokenService;


    // 알림 전송
    @PostMapping("/notifications")
    @Operation(summary = "푸시 알림 전송", description = "특정 FCM 토큰으로 푸시 알림을 전송합니다.")
    public  ResponseEntity<CommonResponse<FcmNotificationResponse>> sendNotificationByToken(@RequestBody FcmSendReqeust fcmSendDto) {
        FcmNotificationResponse response = fcmService.sendPushNotification(fcmSendDto);
        return ResponseEntity.ok(
                CommonResponse.<FcmNotificationResponse>builder()
                        .status(200)
                        .message("푸시 알림이 성공적으로 전송되었습니다.")
                        .data(response)
                        .build()
        );
    }

    // 토큰 등록/갱신
    @PostMapping("/tokens")
    @Operation(summary = "FCM 토큰 등록/갱신", description = "사용자의 FCM 토큰을 등록하거나 갱신합니다.")
    public ResponseEntity<CommonResponse<FcmTokenResponse>>  registerToken(@RequestBody FcmTokenRequest dto) {
        FcmTokenResponse response =fcmTokenService.registerToken(dto.getUserId(), dto.getFcmToken());
        return ResponseEntity.status(HttpStatus.CREATED).body(
                CommonResponse.<FcmTokenResponse>builder()
                        .status(201)
                        .message("FCM 토큰이 성공적으로 등록되었습니다.")
                        .data(response)
                        .build()
        );
    }

    // 토큰 삭제
    @DeleteMapping("/tokens/{fcmToken}")
    @Operation(summary = "FCM 토큰 삭제", description = "지정된 FCM 토큰을 삭제합니다.")
    public ResponseEntity<CommonResponse<Void>>  deleteToken(@RequestParam String fcmToken) {
      fcmTokenService.deleteToken(fcmToken);
        return ResponseEntity.ok(
                CommonResponse.<Void>builder()
                        .status(200)
                        .message("FCM 토큰이 성공적으로 삭제되었습니다.")
                        .data(null)
                        .build()
        );
    }
}