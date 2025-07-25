package com.tave.alarmissue.notification.service;

import com.tave.alarmissue.fcm.dto.request.FcmSendRequest;
import com.tave.alarmissue.notification.domain.NotificationTemplateManager;
import com.tave.alarmissue.notification.dto.request.NotificationCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationBuilderService {

    private final NotificationTemplateManager templateManager;

    //타입별로 알림 메시지 설정
    public FcmSendRequest buildNotificationRequest(NotificationCreateRequest request) {
        // 템플릿을 사용해 제목과 내용 생성
        String title = templateManager.getTitle(request.getNotificationType(), request.getTemplateParams());
        String body = templateManager.getBody(request.getNotificationType(), request.getTemplateParams());

        return FcmSendRequest.builder()
                .token(request.getFcmToken())
                .title(title)
                .body(body)
                .notificationType(request.getNotificationType())
                .relatedEntityId(request.getRelatedEntityId())
                .build();
    }
}
