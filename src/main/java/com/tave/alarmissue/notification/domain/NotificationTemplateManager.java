package com.tave.alarmissue.notification.domain;

import com.tave.alarmissue.notification.domain.enums.NotificationType;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Component
public class NotificationTemplateManager {

    //제목 템플릿
    public String getTitle(NotificationType type, Map<String, Object> params) {
        switch (type) {
            case KEYWORD_NEWS:
                return String.format("[%s] 새로운 뉴스", params.get("keyword"));
            case COMMUNITY_COMMENT:
                return "새로운 댓글이 달렸습니다";
            case COMMUNITY_LIKE:
                return "좋아요를 받았습니다";
//            case COMMUNITY_VOTE_END:
//                return "투표가 마감되었습니다";
            case COMMUNITY_REPLY:
                return "답글이 달렸습니다";
            case ANNOUNCEMENT:
                return "새로운 공지사항";
            default:
                return "알림";
        }
    }

    public String getBody(NotificationType type, Map<String, Object> params) {
        switch (type) {
            case KEYWORD_NEWS:
                return String.format("%s (방금 전)", params.get("newsTitle"));
            case COMMUNITY_COMMENT:
                return String.format("%s님이 댓글을 남겼습니다", params.get("commenterName"));
            case COMMUNITY_LIKE:
                return String.format("%s님이 회원님의 글을 좋아합니다", params.get("likerName"));
//            case COMMUNITY_VOTE_END:
//                return String.format("'%s' 투표가 마감되었습니다",
//                        truncateString((String) params.get("voteTitle"), 20));
            case COMMUNITY_REPLY:
                return String.format("%s님이 답글을 남겼습니다", params.get("replierName"));
            case ANNOUNCEMENT:
                return truncateString((String) params.get("announcementTitle"), 50);
            default:
                return "새로운 알림이 도착했습니다";
        }
    }


    public String getAnalyticsLabel(NotificationType type) {
        String baseLabel = type.name().toLowerCase();
        String dateLabel = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        return baseLabel + "_" + dateLabel;
    }

    private String truncateString(String text, int maxLength) {
        if (text == null || text.length() <= maxLength) {
            return text;
        }
        return text.substring(0, maxLength) + "...";
    }
}

