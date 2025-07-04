package com.tave.alarmissue.news.exceptions;

import com.tave.alarmissue.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum VoteErrorCode implements ErrorCode {

    USER_ID_NOT_FOUND(HttpStatus.BAD_REQUEST, "해당 사용자가 존재하지 않습니다."),
    NEWS_ID_NOT_FOUND(HttpStatus.BAD_REQUEST, "해당 뉴스가 존재하지 않습니다.");


    private final HttpStatus httpStatus;
    private final String message;
}
