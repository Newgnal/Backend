package com.tave.alarmissue.newsroom.exception;

import com.tave.alarmissue.global.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public enum KeywordErrorCode implements ErrorCode {
    KEYWORD_EMPTY("키워드를 입력해주세요.", HttpStatus.BAD_REQUEST),
    KEYWORD_LENGTH_INVALID("키워드는 2자 이상 10자 이하로 입력해주세요.", HttpStatus.BAD_REQUEST),
    KEYWORD_ALREADY_EXISTS("이미 존재하는 키워드입니다: %s", HttpStatus.CONFLICT),
    USER_NOT_FOUND("존재하지 않는 사용자입니다.", HttpStatus.NOT_FOUND),
    KEYWORD_NOT_FOUND("존재하지 않는 키워드입니다", HttpStatus.NOT_FOUND),
    KEYWORD_NOT_REGISTERED("등록되지 않은 키워드입니다: %s", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED_ACCESS("해당 키워드에 접근할 권한이 없습니다.", HttpStatus.FORBIDDEN);

    private final String message;
    private final HttpStatus httpStatus;

    KeywordErrorCode(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getFormattedMessage(Object... args) {
        return String.format(message, args);
    }
}
