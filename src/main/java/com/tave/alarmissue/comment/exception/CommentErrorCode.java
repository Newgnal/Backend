package com.tave.alarmissue.comment.exception;

import com.tave.alarmissue.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum CommentErrorCode implements ErrorCode {
    USER_ID_NOT_FOUND(HttpStatus.BAD_REQUEST, "해당 사용자가 존재하지 않습니다."),
    POST_ID_NOT_FOUND(HttpStatus.BAD_REQUEST,"해당 게시글이 존재하지 않습니다."),
    COMMENT_ID_NOT_FOUND(HttpStatus.BAD_REQUEST,"해당 댓글이 존재하지 않습니다."),
    COMMENT_POST_MISMATCH(HttpStatus.BAD_REQUEST,"댓글과 게시글이 일치하지 않습니다."),
    COMMENT_DELETE_FORBIDDEN(HttpStatus.FORBIDDEN,"댓글 수정 권한이 없습니다.");
    private final HttpStatus httpStatus;
    final String message;

}
