package com.tave.alarmissue.notification.controller;

import com.tave.alarmissue.auth.dto.request.PrincipalUserDetails;
import com.tave.alarmissue.notification.domain.enums.NotificationType;
import com.tave.alarmissue.notification.dto.request.DoNotDisturbRequest;
import com.tave.alarmissue.notification.dto.request.NotificationTypeToggleRequest;
import com.tave.alarmissue.notification.dto.response.UserNotificationSettingResponse;
import com.tave.alarmissue.notification.service.UserNotificationSettingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user-notification/v1")
@Tag(name = "알림 설정 API", description = "사용자 알림 설정 관리")
public class NotificationSettingController {

    private final UserNotificationSettingService settingService;

    @GetMapping
    @Operation(summary = "알림 설정 조회", description = "사용자의 모든 알림 설정을 조회합니다.")
    public ResponseEntity<UserNotificationSettingResponse> getNotificationSettings( @AuthenticationPrincipal PrincipalUserDetails principal) {
        Long userId = principal.getUserId();

        UserNotificationSettingResponse response = settingService.getUserNotificationSetting(userId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/do-not-disturb")
    @Operation(summary = "방해금지 시간 설정", description = "방해금지 시간을 설정합니다.")
    public ResponseEntity<UserNotificationSettingResponse> updateDoNotDisturbSetting(
            @AuthenticationPrincipal PrincipalUserDetails principal,
            @RequestBody @Valid DoNotDisturbRequest request) {
        Long userId = principal.getUserId();

        UserNotificationSettingResponse response = settingService.updateDoNotDisturbSetting(userId, request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/types/{notificationType}")
    @Operation(summary = "알림 타입별 설정", description = "특정 알림 타입의 활성화 여부를 설정합니다.")
    public ResponseEntity<UserNotificationSettingResponse> toggleNotificationType(
            @AuthenticationPrincipal PrincipalUserDetails principal,
            @PathVariable NotificationType notificationType,
            @RequestBody @Valid NotificationTypeToggleRequest request) {
        Long userId = principal.getUserId();

        UserNotificationSettingResponse response = settingService.toggleNotificationType(
                userId, notificationType, request.getEnabled());
        return ResponseEntity.ok(response);
    }
}

