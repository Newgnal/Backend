package com.tave.alarmissue.notification.service;

import com.tave.alarmissue.notification.domain.UserNotificationSetting;
import com.tave.alarmissue.notification.domain.enums.NotificationType;
import com.tave.alarmissue.notification.dto.request.DoNotDisturbRequest;
import com.tave.alarmissue.notification.dto.response.UserNotificationSettingResponse;
import com.tave.alarmissue.notification.exception.NotificationErrorCode;
import com.tave.alarmissue.notification.exception.NotificationException;
import com.tave.alarmissue.notification.repository.UserNotificationSettingRepository;
import com.tave.alarmissue.user.domain.UserEntity;
import com.tave.alarmissue.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserNotificationSettingService {

    private final UserNotificationSettingRepository settingRepository;
    private final UserRepository userRepository;

    //사용자 알림 설정 조회
    public UserNotificationSettingResponse getUserNotificationSetting(Long userId) {
        UserNotificationSetting setting = getOrCreateDefaultSetting(userId);
        return convertToResponse(setting);
    }

    //방해금지 시간 설정
    @Transactional
    public UserNotificationSettingResponse updateDoNotDisturbSetting(Long userId, DoNotDisturbRequest request) {
        UserNotificationSetting setting = getOrCreateSetting(userId);

        //키워드 뉴스 알림 설정이 활성화되어있는지 확인
        if (request.getEnabled() && !setting.getKeywordNewsEnabled()) {
            throw new NotificationException(NotificationErrorCode.KEYWORD_NEWS_REQUIRED_FOR_DND);
        }

        setting.updateDoNotDisturbSetting(request.getEnabled(), request.getStartTime(), request.getEndTime());

        UserNotificationSetting savedSetting = settingRepository.save(setting);
        return convertToResponse(savedSetting);
    }

    // 특정 타입 알림 설정
    @Transactional
    public UserNotificationSettingResponse toggleNotificationType(Long userId, NotificationType type, Boolean enabled) {
        UserNotificationSetting setting = getOrCreateSetting(userId);

        setting.updateNotificationSetting(type, enabled);

        // 키워드 뉴스 알림 비활성화시, 방해금지 시간도 비활성화
        if (type == NotificationType.KEYWORD_NEWS && !enabled) {
            setting.updateDoNotDisturbSetting(false,
                    setting.getDoNotDisturbStartTime(),
                    setting.getDoNotDisturbEndTime());
        }

        UserNotificationSetting savedSetting = settingRepository.save(setting);
        return convertToResponse(savedSetting);
    }

    //특정 타입이 알림 설정되어있는지 확인
    public boolean isNotificationEnabled(Long userId, NotificationType type) {
        return settingRepository.findByUserId(userId)
                .map(setting -> setting.isNotificationEnabled(type))
                .orElse(true); // 기본값: 활성화
    }

    //방해 금지 시간인지 확인
    public boolean isDoNotDisturbTime(Long userId) {
        return settingRepository.findByUserId(userId)
                .map(this::checkDoNotDisturbTime)
                .orElse(false);
    }

    // ------- method ------

    private UserNotificationSetting getOrCreateSetting(Long userId) {
        return settingRepository.findByUserId(userId)
                .orElseGet(() -> createDefaultSetting(userId));
    }

    private UserNotificationSetting getOrCreateDefaultSetting(Long userId) {
        return settingRepository.findByUserId(userId)
                .orElseGet(() -> createDefaultSettingWithoutSaving(userId));
    }

    private UserNotificationSetting createDefaultSetting(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        UserNotificationSetting setting = UserNotificationSetting.builder()
                .user(user)
                .build(); // Builder.Default 값들이 자동 적용

        return settingRepository.save(setting);
    }

    private UserNotificationSetting createDefaultSettingWithoutSaving(Long userId) {
        UserEntity user = UserEntity.builder().id(userId).build(); // proxy 객체로 생성

        return UserNotificationSetting.builder()
                .user(user)
                .build();
    }

    private boolean checkDoNotDisturbTime(UserNotificationSetting setting) {
        if (!setting.getDoNotDisturbEnabled() ||
                setting.getDoNotDisturbStartTime() == null ||
                setting.getDoNotDisturbEndTime() == null) {
            return false;
        }

        LocalTime now = LocalTime.now();
        LocalTime startTime = setting.getDoNotDisturbStartTime();
        LocalTime endTime = setting.getDoNotDisturbEndTime();

        // 자정을 넘나드는 경우 처리 (예: 22:00 ~ 07:00)
        if (startTime.isAfter(endTime)) {
            return now.isAfter(startTime) || now.isBefore(endTime);
        } else {
            return now.isAfter(startTime) && now.isBefore(endTime);
        }
    }

    private UserNotificationSettingResponse convertToResponse(UserNotificationSetting setting) {
        return UserNotificationSettingResponse.builder()
                .doNotDisturbEnabled(setting.getDoNotDisturbEnabled())
                .doNotDisturbStartTime(setting.getDoNotDisturbStartTime())
                .doNotDisturbEndTime(setting.getDoNotDisturbEndTime())
                .keywordNewsEnabled(setting.getKeywordNewsEnabled())
                .communityCommentEnabled(setting.getCommunityCommentEnabled())
                .communityLikeEnabled(setting.getCommunityLikeEnabled())
//                .communityVoteEndEnabled(setting.getCommunityVoteEndEnabled())
                .communityReplyEnabled(setting.getCommunityReplyEnabled())
                .announcementEnabled(setting.getAnnouncementEnabled())
                .build();
    }
}

