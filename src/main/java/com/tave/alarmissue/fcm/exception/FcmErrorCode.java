package com.tave.alarmissue.fcm.exception;

import com.tave.alarmissue.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum FcmErrorCode implements ErrorCode {

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "FCM 토큰을 찾을 수 없습니다."),
    PUSH_SEND_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "푸시 알림 전송에 실패했습니다."),
    INVALID_FCM_TOKEN(HttpStatus.BAD_REQUEST, "유효하지 않은 FCM 토큰입니다."),
    TOKEN_OWNERSHIP_MISMATCH(HttpStatus.FORBIDDEN, "해당 토큰에 대한 권한이 없습니다."),
    FCM_SERVICE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, "FCM 서비스가 일시적으로 사용할 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}