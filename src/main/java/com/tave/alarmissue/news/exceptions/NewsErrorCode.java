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
    INVALID_REQUEST(HttpStatus.BAD_REQUEST,"대댓글에는 답글을 달 수 없습니다."),

    INVALID_DATE_RANGE(HttpStatus.BAD_REQUEST, "시작날짜는 종료날짜보다 이전이어야 합니다."),
    DATE_RANGE_TOO_LONG(HttpStatus.BAD_REQUEST, "조회 기간은 최대 1년까지 가능합니다."),
    INVALID_DATE_FORMAT(HttpStatus.BAD_REQUEST, "날짜 형식이 올바르지 않습니다. (yyyy-MM-dd)"),
    ANALYTICS_DATA_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 기간에 분석할 데이터가 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
