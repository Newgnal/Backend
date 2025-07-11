package com.tave.alarmissue.post.exception;

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
    COMMENT_DELETE_FORBIDDEN(HttpStatus.FORBIDDEN,"댓글 삭제 권한이 존재하지 않습니다.");
    private final HttpStatus httpStatus;
    final String message;

}
