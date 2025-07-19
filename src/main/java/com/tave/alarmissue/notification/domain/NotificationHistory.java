package com.tave.alarmissue.notification.domain;

import com.tave.alarmissue.global.domain.BaseTimeEntity;
import com.tave.alarmissue.notification.domain.enums.NotificationStatus;
import com.tave.alarmissue.notification.domain.enums.NotificationType;
import com.tave.alarmissue.user.domain.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Table(name = "notification_history")
public class NotificationHistory extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType notificationType;

    @Column(nullable = false, length = 500)
    private String title;

    @Column(nullable = false, length = 1000)
    private String body;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private NotificationStatus status;

    @Column
    private String fcmMessageId; // Firebase에서 반환하는 메시지 ID

    @Column(columnDefinition = "TEXT")
    private String failureReason;

    @Column
    private Long relatedEntityId;

    // 읽음 처리 관련
    @Column(nullable = false)
    @Builder.Default
    private Boolean isRead = false;

    @Column
    private LocalDateTime readAt;

    // 비즈니스 메서드
    public void markAsRead() {
        this.isRead = true;
        this.readAt = LocalDateTime.now();
    }

    public String getTimeAgo() {
        return TimeUtils.getTimeAgo(this.getCreatedAt());
    }
}
