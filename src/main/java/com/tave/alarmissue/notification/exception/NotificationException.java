package com.tave.alarmissue.notification.exception;

import com.tave.alarmissue.global.exception.CustomException;

public class NotificationException extends CustomException {

    private final NotificationErrorCode errorCode;

    public NotificationException(NotificationErrorCode errorCode) {
        super(errorCode);
        this.errorCode = errorCode;
    }

    public NotificationErrorCode getErrorCode() {
        return errorCode;
    }
}

