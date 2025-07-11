package com.tave.alarmissue.post.exception;

import com.tave.alarmissue.global.exception.CustomException;
import com.tave.alarmissue.global.exception.ErrorCode;

public class CommentException extends CustomException {
    public CommentException(ErrorCode errorCode) {super(errorCode);}

    public CommentException(ErrorCode errorCode, String message) {super(errorCode, message);}
}
