package com.tave.alarmissue.fcm.service;


import com.google.firebase.messaging.*;
import com.tave.alarmissue.fcm.dto.request.FcmSendReqeust;
import org.springframework.stereotype.Service;

@Service
public class FcmService {

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
            // 만료/삭제된 토큰 처리
            if (ex.getMessagingErrorCode() == MessagingErrorCode.UNREGISTERED) {
                System.err.println("해당 토큰이 만료되었거나 등록 해제됨: " + fcmSendDto.getToken());
            }
            // FCM 서비스 일시 중단
            else if (ex.getMessagingErrorCode() == MessagingErrorCode.UNAVAILABLE) {
                System.err.println("FCM 서비스가 일시적으로 중단됨. 나중에 재시도 필요.");
            }
            else {
                System.err.println("FCM 발송 실패: " + ex.getMessagingErrorCode() + " - " + ex.getMessage());
            }
        } catch (Exception e) {
            System.err.println("알 수 없는 오류 발생: " + e.getMessage());
        }
    }
}