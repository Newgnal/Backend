package com.tave.alarmissue.security.exception;

import com.tave.alarmissue.global.exception.CustomException;

public class TokenException extends CustomException {

  private final SecurityErrorCode errorCode;

  public TokenException(SecurityErrorCode errorCode) {
    super(errorCode);
      this.errorCode = errorCode;
  }

  public SecurityErrorCode getErrorCode() {
    return errorCode;
  }
}


