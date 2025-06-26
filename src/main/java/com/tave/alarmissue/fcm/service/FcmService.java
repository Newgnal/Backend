package com.tave.alarmissue.fcm.service;


import com.google.firebase.messaging.*;
import com.tave.alarmissue.fcm.dto.request.FcmSendReqeust;
import com.tave.alarmissue.fcm.exception.FcmErrorCode;
import com.tave.alarmissue.fcm.exception.FcmException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FcmService {
    private final FcmTokenService fcmTokenService;

    public void sendPushNotification(FcmSendReqeust fcmSendDto) {
        Notification notification = Notification.builder()
                .setTitle(fcmSendDto.getTitle())
                .setBody(fcmSendDto.getBody())
                .build();


        Message message = Message.builder()
                .setToken(fcmSendDto.getToken()) // 특정 기기 토큰 (fcm token)
                .setNotification(notification)
                .build();
        try {
            String response = FirebaseMessaging.getInstance().send(message);
            System.out.println("Successfully sent message: " + response);
        } catch (FirebaseMessagingException ex) {
            handleFirebaseMessagingException(ex, fcmSendDto.getToken());
        } catch (Exception e) {
            throw new FcmException(FcmErrorCode.PUSH_SEND_FAILED);
        }
    }

    private void handleFirebaseMessagingException(FirebaseMessagingException ex, String token) {
        MessagingErrorCode errorCode = ex.getMessagingErrorCode();

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
}