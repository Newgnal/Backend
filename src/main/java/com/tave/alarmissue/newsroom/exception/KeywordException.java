package com.tave.alarmissue.newsroom.exception;

import com.tave.alarmissue.global.exception.CustomException;

public class KeywordException extends CustomException {
    private final KeywordErrorCode errorCode;

    public KeywordException(KeywordErrorCode errorCode) {
        super(errorCode);
        this.errorCode = errorCode;
    }

    public KeywordException(KeywordErrorCode errorCode, Object... args) {
        super(errorCode);
        this.errorCode = errorCode;
    }

    public KeywordErrorCode getErrorCode() {
        return errorCode;
    }
}
