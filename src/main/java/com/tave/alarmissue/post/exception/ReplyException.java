package com.tave.alarmissue.post.exception;

import com.tave.alarmissue.global.exception.CustomException;
import com.tave.alarmissue.global.exception.ErrorCode;

public class ReplyException extends CustomException {
    public ReplyException(ErrorCode errorCode) {
        super(errorCode);
    }
    public ReplyException(ErrorCode errorCode, String message) {super(errorCode, message);}
}
