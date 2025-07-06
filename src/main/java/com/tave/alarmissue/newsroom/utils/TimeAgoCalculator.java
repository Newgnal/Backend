package com.tave.alarmissue.newsroom.utils;

import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

@Component
public class TimeAgoCalculator {

    public static String calculateTimeAgo(LocalDateTime publishedDate) {
        if (publishedDate == null) {
            return "시간 정보 없음";
        }

        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(publishedDate, now);

        long days = duration.toDays();
        long hours = duration.toHours();
        long minutes = duration.toMinutes();

        // 오늘 발행된 뉴스인지 확인
        if (publishedDate.toLocalDate().equals(now.toLocalDate())) {
            // 오늘 발행된 뉴스 - 시간 단위로 표시
            if (hours == 0) {
                if (minutes == 0) {
                    return "방금 전";
                } else {
                    return minutes + "분 전";
                }
            } else {
                return hours + "시간 전";
            }
        } else {
            // 오늘이 아닌 날 발행된 뉴스 - 일 단위로 표시
            if (days == 1) {
                return "1일 전";
            } else if (days < 7) {
                return days + "일 전";
            } else if (days < 30) {
                long weeks = days / 7;
                return weeks + "주 전";
            } else if (days < 365) {
                long months = days / 30;
                return months + "개월 전";
            } else {
                long years = days / 365;
                return years + "년 전";
            }
        }
    }
}
