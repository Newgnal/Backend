package com.tave.alarmissue.fcm.exception;

import com.tave.alarmissue.global.exception.CustomException;

public class FcmException extends CustomException {

    private final FcmErrorCode errorCode;

    public FcmException(FcmErrorCode errorCode) {
        super(errorCode);
        this.errorCode = errorCode;
    }

    public FcmErrorCode getErrorCode() {
        return errorCode;
    }
}