package com.tave.alarmissue.news.exceptions;

import com.tave.alarmissue.global.exception.CustomException;
import com.tave.alarmissue.global.exception.ErrorCode;

public class NewsVoteException extends CustomException {

    public NewsVoteException(ErrorCode errorCode) {
        super(errorCode);
    }
    public NewsVoteException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
