package com.tave.alarmissue.notification.domain.enums;

public enum NotificationStatus {
    SUCCESS("전송 성공"),
    FAILED("전송 실패"),
    PENDING("전송 대기");

    private final String description;

    NotificationStatus(String description) {
        this.description = description;
    }
}
