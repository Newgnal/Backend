package com.tave.alarmissue.notification.service;


import com.tave.alarmissue.notification.domain.NotificationHistory;
import com.tave.alarmissue.notification.domain.enums.NotificationType;
import com.tave.alarmissue.notification.dto.request.NotificationHistoryRequest;
import com.tave.alarmissue.notification.dto.response.NotificationListResponse;
import com.tave.alarmissue.notification.dto.response.NotificationResponse;
import com.tave.alarmissue.notification.repository.NotificationHistoryRepository;
import com.tave.alarmissue.user.domain.UserEntity;
import com.tave.alarmissue.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationHistoryService {

    private final NotificationHistoryRepository notificationHistoryRepository;
    private final UserRepository userRepository;

    /**
     * 알림 이력 저장
     */
    @Transactional
    public NotificationHistory saveNotificationHistory(NotificationHistoryRequest request) {
        UserEntity user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        NotificationHistory history = NotificationHistory.builder()
                .user(user)
                .notificationType(request.getNotificationType())
                .title(request.getTitle())
                .body(request.getBody())
                .status(request.getStatus())
                .fcmMessageId(request.getFcmMessageId())
                .failureReason(request.getFailureReason())
                .relatedEntityId(request.getRelatedEntityId())
                .build();

        return notificationHistoryRepository.save(history);
    }

    /**
     * 사용자별 알림 목록 조회
     */
    public NotificationListResponse getNotificationList(Long userId, Pageable pageable) {
        Page<NotificationHistory> historyPage = notificationHistoryRepository
                .findByUserIdOrderByCreatedAtDesc(userId, pageable);

        List<NotificationResponse> notifications = historyPage.getContent().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());

        return NotificationListResponse.builder()
                .notifications(notifications)
                .totalCount(historyPage.getTotalElements())
                .hasNext(historyPage.hasNext())
                .unreadCount(getUnreadCount(userId))
                .build();
    }


    /**
     * 읽지 않은 알림 개수 조회
     */
    public long getUnreadCount(Long userId) {
        return notificationHistoryRepository.countByUserIdAndIsReadFalse(userId);
    }

    /**
     * 알림 읽음 처리
     */
    @Transactional
    public void markAsRead(Long userId, Long notificationId) {
        NotificationHistory history = notificationHistoryRepository.findById(notificationId)
                .orElseThrow(() -> new IllegalArgumentException("알림을 찾을 수 없습니다."));

        // 본인의 알림인지 확인
        if (!history.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }

        history.markAsRead();
        notificationHistoryRepository.save(history);
    }

    private NotificationResponse convertToResponse(NotificationHistory history) {
        return NotificationResponse.builder()
                .id(history.getId())
                .type(history.getNotificationType())
                .typeName(history.getNotificationType().getDisplayName())
                .title(history.getTitle())
                .body(history.getBody())
                .isRead(history.getIsRead())
                .timeAgo(history.getTimeAgo())
                .createdAt(history.getCreatedAt())
                .readAt(history.getReadAt())
                .relatedEntityId(history.getRelatedEntityId())
                .build();
    }


}

