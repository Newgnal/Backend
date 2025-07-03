package com.tave.alarmissue.like.exception;

import com.tave.alarmissue.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum LikeErrorCode implements ErrorCode {

    USER_ID_NOT_FOUND(HttpStatus.BAD_REQUEST, "해당 사용자가 존재하지 않습니다."),
    POST_ID_NOT_FOUND(HttpStatus.BAD_REQUEST,"해당 게시글이 존재하지 않습니다."),
    COMMENT_ID_NOT_FOUND(HttpStatus.BAD_REQUEST,"해당 댓글이 존재하지 않습니다."),
    DIFFERENT_POST_ID_COMMENT_ID(HttpStatus.BAD_REQUEST,"좋아요 권한이 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
