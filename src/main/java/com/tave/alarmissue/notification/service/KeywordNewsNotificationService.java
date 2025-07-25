package com.tave.alarmissue.notification.service;

import com.tave.alarmissue.fcm.dto.request.FcmSendRequest;
import com.tave.alarmissue.fcm.service.FcmService;
import com.tave.alarmissue.fcm.service.FcmTokenService;
import com.tave.alarmissue.news.domain.News;
import com.tave.alarmissue.newsroom.entity.Keyword;
import com.tave.alarmissue.newsroom.repository.KeywordRepository;
import com.tave.alarmissue.notification.domain.enums.NotificationType;
import com.tave.alarmissue.notification.dto.request.NotificationCreateRequest;
import com.tave.alarmissue.user.domain.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class KeywordNewsNotificationService {

    private final KeywordRepository keywordRepository;
    private final FcmTokenService fcmTokenService;
    private final NotificationBuilderService notificationBuilderService;
    private final FcmService fcmService;
    private final UserNotificationSettingService notificationSettingService;

    // 알림 전송
    @Async
    @Transactional
    public void sendKeywordNewsNotifications(News news) {
        log.info("키워드 뉴스 알림 처리 시작 - 뉴스 ID: {}, 제목: {}", news.getId(), news.getTitle());

        try {
            // 1. 뉴스 제목에서 키워드 매칭
            List<String> matchedKeywords = findMatchingKeywords(news);

            if (matchedKeywords.isEmpty()) {
                log.debug("매칭되는 키워드가 없음 - 뉴스 ID: {}", news.getId());
                return;
            }

            log.info("매칭된 키워드: {} - 뉴스 ID: {}", matchedKeywords, news.getId());

            // 2. 매칭된 키워드별로 알림 전송
            for (String keywordText : matchedKeywords) {
                sendNotificationsForKeyword(news, keywordText);
            }

        } catch (Exception e) {
            log.error("키워드 뉴스 알림 처리 중 오류 발생 - 뉴스 ID: {}", news.getId(), e);
        }
    }

    // ------- method ------

    // 뉴스 제목에서 키워드 찾기
    private List<String> findMatchingKeywords(News news) {
        // 활성화된 모든 키워드 조회
        List<String> activeKeywords = keywordRepository.findAllActiveKeywords();

        String newsTitle = news.getTitle().toLowerCase();

        return activeKeywords.stream()
                .filter(keyword -> newsTitle.contains(keyword.toLowerCase()))
                .distinct()
                .collect(Collectors.toList());
    }

    //특정 키워드에 대한 알림 전송 메서드
    private void sendNotificationsForKeyword(News news, String keywordText) {
        List<Keyword> keywords = keywordRepository.findByKeywordAndNotificationEnabledTrue(keywordText);

        log.info("키워드 '{}' 알림 대상 사용자 수: {}", keywordText, keywords.size());

        for (Keyword keyword : keywords) {
            try {
                sendNotificationToUser(news, keyword);
            } catch (Exception e) {
                log.error("사용자별 알림 전송 실패 - 사용자 ID: {}, 키워드: {}",
                        keyword.getUser().getId(), keyword.getKeyword(), e);
            }
        }
    }

    //개별 사용자 알림 전송 메서드
    private void sendNotificationToUser(News news, Keyword keyword) {
        UserEntity user = keyword.getUser();

        //1. 키워드 뉴스 알림(전체)이 활성화 되어있는지 체크
        if (!notificationSettingService.isNotificationEnabled(user.getId(), NotificationType.KEYWORD_NEWS)) {
            log.debug("전체 키워드 뉴스 알림이 비활성화됨 - 사용자 ID: {}", user.getId());
            return;
        }

        //2. 개별 키워드 뉴스 알림이 활성화 되어있는지 체크
        if (!keyword.getNotificationEnabled()) {
            log.debug("개별 키워드 '{}' 알림이 비활성화됨 - 사용자 ID: {}",
                    keyword.getKeyword(), user.getId());
            return;
        }

        //3. 방해금지 시간인지 체크
        if (notificationSettingService.isDoNotDisturbTime(user.getId())) {
            log.debug("방해금지 시간으로 알림 전송 스킵 - 사용자 ID: {}", user.getId());
            return;
        }

        // 사용자의 FCM 토큰 조회
        List<String> fcmTokens = fcmTokenService.getUserTokens(user.getId());

        if (fcmTokens.isEmpty()) {
            log.warn("FCM 토큰이 없는 사용자 - 사용자 ID: {}", user.getId());
            return;
        }

        // 각 토큰으로 알림 전송
        for (String fcmToken : fcmTokens) {
            sendSingleNotification(news, keyword, fcmToken);
        }
    }

    // 알림 전송 메서드(실제 전송 로직)
    private void sendSingleNotification(News news, Keyword keyword, String fcmToken) {
        try {
            // 알림 생성 요청 구성
            NotificationCreateRequest createRequest = NotificationCreateRequest.builder()
                    .fcmToken(fcmToken)
                    .notificationType(NotificationType.KEYWORD_NEWS)
                    .relatedEntityId(news.getId())
                    .templateParams(Map.of(
                            "keyword", keyword.getKeyword(),
                            "newsTitle", news.getTitle()
                    ))
                    .build();

            // 알림 메시지 생성
            FcmSendRequest fcmRequest = notificationBuilderService.buildNotificationRequest(createRequest);

            // FCM 전송
            fcmService.sendPushNotification(fcmRequest);

            log.debug("키워드 뉴스 알림 전송 성공 - 사용자 ID: {}, 키워드: {}",
                    keyword.getUser().getId(), keyword.getKeyword());

        } catch (Exception e) {
            log.error("단일 알림 전송 실패 - 사용자 ID: {}, 키워드: {}, 토큰: {}",
                    keyword.getUser().getId(), keyword.getKeyword(), fcmToken, e);
        }
    }
}
