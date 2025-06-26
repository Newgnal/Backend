package com.tave.alarmissue.post.exception;

import com.tave.alarmissue.global.exception.CustomException;
import com.tave.alarmissue.global.exception.ErrorCode;

public class PostException extends CustomException {

    public PostException(ErrorCode errorCode) {
        super(errorCode);
    }

    public PostException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

}
