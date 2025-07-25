package com.tave.alarmissue.notification.domain;

import com.tave.alarmissue.global.domain.BaseTimeEntity;
import com.tave.alarmissue.notification.domain.enums.NotificationType;
import com.tave.alarmissue.user.domain.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Table(name = "user_notification_setting")
public class UserNotificationSetting extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private UserEntity user;

    // 방해 금지 시간
    @Column(nullable = false)
    @Builder.Default
    private Boolean doNotDisturbEnabled = false;

    @Column
    private LocalTime doNotDisturbStartTime; // 예: 22:00

    @Column
    private LocalTime doNotDisturbEndTime;   // 예: 07:00

    // 각 알림 별 활성화 여부
    @Column(nullable = false)
    @Builder.Default
    private Boolean keywordNewsEnabled = true;

    @Column(nullable = false)
    @Builder.Default
    private Boolean communityCommentEnabled = true;

    @Column(nullable = false)
    @Builder.Default
    private Boolean communityLikeEnabled = true;

//    @Column(nullable = false)
//    @Builder.Default
//    private Boolean communityVoteEndEnabled = true;

    @Column(nullable = false)
    @Builder.Default
    private Boolean communityReplyEnabled = true;

    @Column(nullable = false)
    @Builder.Default
    private Boolean announcementEnabled = true;

    // === 비즈니스 메서드 ===
    public void updateDoNotDisturbSetting(Boolean enabled, LocalTime startTime, LocalTime endTime) {
        this.doNotDisturbEnabled = enabled;
        this.doNotDisturbStartTime = startTime;
        this.doNotDisturbEndTime = endTime;
    }

    public void updateNotificationSetting(NotificationType type, Boolean enabled) {
        switch (type) {
            case KEYWORD_NEWS:
                this.keywordNewsEnabled = enabled;
                break;
            case COMMUNITY_COMMENT:
                this.communityCommentEnabled = enabled;
                break;
            case COMMUNITY_LIKE:
                this.communityLikeEnabled = enabled;
                break;
//            case COMMUNITY_VOTE_END:
//                this.communityVoteEndEnabled = enabled;
//                break;
            case COMMUNITY_REPLY:
                this.communityReplyEnabled = enabled;
                break;
            case ANNOUNCEMENT:
                this.announcementEnabled = enabled;
                break;
        }
    }

    public Boolean isNotificationEnabled(NotificationType type) {
        switch (type) {
            case KEYWORD_NEWS:
                return this.keywordNewsEnabled;
            case COMMUNITY_COMMENT:
                return this.communityCommentEnabled;
            case COMMUNITY_LIKE:
                return this.communityLikeEnabled;
//            case COMMUNITY_VOTE_END:
//                return this.communityVoteEndEnabled;
            case COMMUNITY_REPLY:
                return this.communityReplyEnabled;
            case ANNOUNCEMENT:
                return this.announcementEnabled;
            default:
                return false;
        }
    }
}

