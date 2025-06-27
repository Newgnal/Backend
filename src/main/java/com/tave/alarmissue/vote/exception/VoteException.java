package com.tave.alarmissue.vote.exception;

import com.tave.alarmissue.global.exception.CustomException;
import com.tave.alarmissue.global.exception.ErrorCode;

public class VoteException extends CustomException {

    public VoteException(ErrorCode errorCode) {
        super(errorCode);
    }
    public VoteException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
