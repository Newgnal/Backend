package com.tave.alarmissue.news.exceptions;

import com.google.api.Http;
import com.tave.alarmissue.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter


public enum NewsErrorCode implements ErrorCode {
    USER_ID_NOT_FOUND(HttpStatus.BAD_REQUEST, "해당 사용자가 존재하지 않습니다."),
    NEWS_ID_NOT_FOUND(HttpStatus.BAD_REQUEST, "해당 뉴스가 존재하지 않습니다."),
    COMMENT_ID_NOT_FOUND(HttpStatus.BAD_REQUEST,"해당 댓글이 존재하지 않습니다."),
    VOTE_NOT_FOUND(HttpStatus.BAD_REQUEST,"투표가 존재하지 않습니다."),
    COMMENT_ACCESS_DENIED(HttpStatus.FORBIDDEN,"해당 댓글에 권한이 없습니다."),

    INVALID_REQUEST(HttpStatus.BAD_REQUEST,"대댓글에는 답글을 달 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
