package com.tave.alarmissue.news.util;

import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

@Component
public class TimeAgoUtil {

    public static String getTimeAgo(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "";
        }
        //현재시간
        LocalDateTime now = LocalDateTime.now();
        //두 시간 사이의 차이 계산
        Duration duration=Duration.between(dateTime,now);
        long seconds=duration.getSeconds();
        long minutes=seconds/60;
        long hours = minutes / 60;
        long days = hours / 24;
        long weeks = days / 7;
        long months = days / 30;
        long years = days / 365;

        if (seconds < 60) {
            return "방금 전";
        } else if (minutes < 60) {
            return minutes + "분 전";
        } else if (hours < 24) {
            return hours + "시간 전";
        } else if (days < 7) {
            return days + "일 전";
        } else if (weeks < 4) {
            return weeks + "주 전";
        } else if (months < 12) {
            return months + "개월 전";
        } else {
            return years + "년 전";
        }
    }
}
