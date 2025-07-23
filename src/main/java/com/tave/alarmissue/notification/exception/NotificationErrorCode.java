package com.tave.alarmissue.notification.exception;

import com.tave.alarmissue.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum NotificationErrorCode implements ErrorCode {

    DO_NOT_DISTURB_INVALID_STATE(HttpStatus.BAD_REQUEST,
            "키워드 뉴스 알림이 비활성화된 상태에서는 방해금지 시간을 설정할 수 없습니다."),

    KEYWORD_NEWS_REQUIRED_FOR_DND(HttpStatus.BAD_REQUEST,
            "방해금지 시간 기능은 키워드 뉴스 알림이 활성화된 경우에만 사용할 수 있습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
