package com.tave.alarmissue.user.exception;

import com.tave.alarmissue.global.exception.CustomException;
import com.tave.alarmissue.global.exception.ErrorCode;

public class UserException extends CustomException {
    public UserException(ErrorCode errorCode) {super(errorCode);}

    public UserException(ErrorCode errorCode, String message) {super(errorCode, message);}
}
