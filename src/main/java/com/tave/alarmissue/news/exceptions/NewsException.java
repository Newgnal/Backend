package com.tave.alarmissue.news.exceptions;

import com.tave.alarmissue.global.exception.CustomException;
import com.tave.alarmissue.global.exception.ErrorCode;

public class NewsException extends CustomException {

    public NewsException(ErrorCode errorCode) {
        super(errorCode);
    }
    public NewsException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
