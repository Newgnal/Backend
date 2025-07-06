package com.tave.alarmissue.newsroom.exception;

public class KeywordException extends RuntimeException {
    private final KeywordErrorCode errorCode;

    public KeywordException(KeywordErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public KeywordException(KeywordErrorCode errorCode, Object... args) {
        super(errorCode.getFormattedMessage(args));
        this.errorCode = errorCode;
    }

    public KeywordErrorCode getErrorCode() {
        return errorCode;
    }
}
