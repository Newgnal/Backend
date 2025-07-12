package com.tave.alarmissue.news.exceptions;

import com.tave.alarmissue.global.exception.CustomException;
import com.tave.alarmissue.global.exception.ErrorCode;

public class NewsCommentException extends CustomException {
    public NewsCommentException(ErrorCode errorCode) {
        super(errorCode);
    }
    public NewsCommentException(ErrorCode errorCode, String message){
        super(errorCode,message);
    }
}
