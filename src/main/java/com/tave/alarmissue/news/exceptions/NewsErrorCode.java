package com.tave.alarmissue.news.exceptions;

import com.tave.alarmissue.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum NewsErrorCode implements ErrorCode {
    USER_ID_NOT_FOUND(HttpStatus.BAD_REQUEST, "해당 사용자가 존재하지 않습니다."),
    NEWS_ID_NOT_FOUND(HttpStatus.BAD_REQUEST,"해당 뉴스가 존재하지 않습니다."),
    COMMENT_ID_NOT_FOUND(HttpStatus.BAD_REQUEST,"해당 댓글이 존재하지 않습니다."),
    VOTE_NOT_FOUND(HttpStatus.BAD_REQUEST,"투표가 존재하지 않습니다."),
    UNAUTHORIZED_DELETE(HttpStatus.BAD_REQUEST,"댓글 삭제 권한이 없습니다.");


    private final HttpStatus httpStatus;
    final String message;
}
