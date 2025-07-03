package com.tave.alarmissue.like.exception;

import com.tave.alarmissue.global.exception.CustomException;
import com.tave.alarmissue.global.exception.ErrorCode;

public class LikeException extends CustomException {
  public LikeException(ErrorCode errorCode) {
    super(errorCode);
  }

  public LikeException(ErrorCode errorCode, String message) {
    super(errorCode, message);
  }
}
