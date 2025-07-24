package com.tave.alarmissue.fcm.service;


import com.google.firebase.ErrorCode;
import com.google.firebase.messaging.*;
import com.tave.alarmissue.fcm.dto.request.FcmSendRequest;
import com.tave.alarmissue.fcm.dto.response.FcmNotificationResponse;
import com.tave.alarmissue.fcm.exception.FcmErrorCode;
import com.tave.alarmissue.fcm.exception.FcmException;
import com.tave.alarmissue.notification.domain.NotificationTemplateManager;
import com.tave.alarmissue.notification.domain.enums.NotificationStatus;
import com.tave.alarmissue.notification.domain.enums.NotificationType;
import com.tave.alarmissue.notification.dto.request.NotificationHistoryRequest;
import com.tave.alarmissue.notification.service.NotificationHistoryService;
import com.tave.alarmissue.notification.service.UserNotificationSettingService;
import com.tave.alarmissue.user.domain.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class FcmService {
    private final FcmTokenService fcmTokenService;
    private final NotificationHistoryService notificationHistoryService;
    private final UserNotificationSettingService settingService;
    private final NotificationTemplateManager templateManager;

    public FcmNotificationResponse sendPushNotification(FcmSendRequest fcmSendDto) {
        UserEntity user = fcmTokenService.getUserByToken(fcmSendDto.getToken());
        Long userId = user.getId();

        //알림 설정 확인
        if (!isNotificationAllowed(userId, fcmSendDto.getNotificationType())) {
            // 이력만 저장하고 전송하지 않음
            saveNotificationHistory(fcmSendDto, null, NotificationStatus.BLOCKED, "사용자 설정에 의해 차단됨", userId);
            return null;
        }

        Notification notification = Notification.builder()
                .setTitle(fcmSendDto.getTitle())
                .setBody(fcmSendDto.getBody())
                .build();

        AndroidConfig androidConfig = AndroidConfig.builder()
                .setFcmOptions(AndroidFcmOptions.builder()
                        .setAnalyticsLabel(templateManager.getAnalyticsLabel(fcmSendDto.getNotificationType()))
                        .build())
                .build();

        Message message = Message.builder()
                .setToken(fcmSendDto.getToken()) // 특정 기기 토큰 (fcm token)
                .setNotification(notification)
                .setAndroidConfig(androidConfig)
                .build();
        try {
            String messageId = FirebaseMessaging.getInstance().send(message);
            System.out.println("Successfully sent message: " + messageId);

            //성공 이력 저장
            saveNotificationHistory(fcmSendDto, messageId, NotificationStatus.SUCCESS, null, userId);

            return FcmNotificationResponse.builder()
                    .messageId(messageId)
                    .fcmToken(fcmSendDto.getToken())
                    .sentAt(LocalDateTime.now())
                    .build();

        } catch (FirebaseMessagingException ex) {
            saveNotificationHistory(fcmSendDto, null, NotificationStatus.FAILED, ex.getMessage(),userId);
            handleFirebaseMessagingException(ex, fcmSendDto.getToken());
        } catch (Exception e) {
            saveNotificationHistory(fcmSendDto, null, NotificationStatus.FAILED, e.getMessage(),userId);
            throw new FcmException(FcmErrorCode.PUSH_SEND_FAILED);
        }
        return null;
    }

    // ----- method -----

    private void saveNotificationHistory(FcmSendRequest request, String messageId,
                                         NotificationStatus status, String failureReason, Long userId) {
        try {
            NotificationHistoryRequest historyRequest = NotificationHistoryRequest.builder()
                    .userId(userId)
                    .notificationType(request.getNotificationType())
                    .title(request.getTitle())
                    .body(request.getBody())
                    .status(status)
                    .fcmMessageId(messageId)
                    .failureReason(failureReason)
                    .relatedEntityId(request.getRelatedEntityId())
                    .build();

            notificationHistoryService.saveNotificationHistory(historyRequest);
        } catch (Exception e) {
            // 이력 저장 실패는 메인 로직에 영향주지 않도록
            System.err.println("알림 이력 저장 실패: " + e.getMessage());
        }
    }

    private void handleFirebaseMessagingException(FirebaseMessagingException ex, String token) {
        MessagingErrorCode errorCode = ex.getMessagingErrorCode();

        if (errorCode == null) {
            ErrorCode generalErrorCode = ex.getErrorCode();
            System.err.println("MessagingErrorCode is null, using general ErrorCode: " + generalErrorCode);

            throw new FcmException(FcmErrorCode.PUSH_SEND_FAILED);
        }

        switch (errorCode) {
            case UNREGISTERED:
                // 만료/삭제된 토큰은 DB에서 제거
                fcmTokenService.deleteToken(token);
                throw new FcmException(FcmErrorCode.INVALID_FCM_TOKEN);

            case INVALID_ARGUMENT:
                // 메시지 페이로드가 유효한 경우에만 토큰 문제로 간주
                if (isTokenRelatedError(ex.getMessage())) {
                    fcmTokenService.deleteToken(token);
                    throw new FcmException(FcmErrorCode.INVALID_FCM_TOKEN);
                } else {
                    throw new FcmException(FcmErrorCode.PUSH_SEND_FAILED);
                }

            case UNAVAILABLE:
            case INTERNAL:
                throw new FcmException(FcmErrorCode.FCM_SERVICE_UNAVAILABLE);

            default:
                throw new FcmException(FcmErrorCode.PUSH_SEND_FAILED);
        }
    }

    private boolean isTokenRelatedError(String errorMessage) {
        // Firebase 공식 문서에 따른 토큰 관련 에러 메시지 패턴 확인
        return errorMessage != null &&
                errorMessage.contains("registration token is not a valid FCM registration token");
    }

    //알림 전송 허용 여부 확인
    private boolean isNotificationAllowed(Long userId, NotificationType type) {
        // 알림 타입이 활성화되어 있는지 확인
        if (!settingService.isNotificationEnabled(userId, type)) {
            return false;
        }

        // 방해금지 시간 확인
        if (type.isSupportDoNotDisturb() && settingService.isDoNotDisturbTime(userId)) {
            return false;
        }

        return true;
    }

}