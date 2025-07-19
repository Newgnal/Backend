package com.tave.alarmissue.notification.repository;


import com.tave.alarmissue.notification.domain.NotificationHistory;
import com.tave.alarmissue.notification.domain.enums.NotificationStatus;
import com.tave.alarmissue.notification.domain.enums.NotificationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface NotificationHistoryRepository extends JpaRepository<NotificationHistory, Long> {

    // 사용자별 알림 목록 조회 (최신순, 페이징)
    Page<NotificationHistory> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);

    // 사용자별 읽지 않은 알림 개수
    long countByUserIdAndIsReadFalse(Long userId);

    // 사용자별 알림 타입별 조회
    Page<NotificationHistory> findByUserIdAndNotificationTypeOrderByCreatedAtDesc(
            Long userId, NotificationType type, Pageable pageable);

    // 사용자별 읽지 않은 알림 조회
    Page<NotificationHistory> findByUserIdAndIsReadFalseOrderByCreatedAtDesc(
            Long userId, Pageable pageable);

    // 성공적으로 전송된 알림만 조회
    Page<NotificationHistory> findByUserIdAndStatusOrderByCreatedAtDesc(
            Long userId, NotificationStatus status, Pageable pageable);
}
