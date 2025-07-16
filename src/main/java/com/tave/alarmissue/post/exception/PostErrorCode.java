package com.tave.alarmissue.post.exception;

import com.tave.alarmissue.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum PostErrorCode implements ErrorCode {

    USER_ID_NOT_FOUND(HttpStatus.BAD_REQUEST, "해당 사용자가 존재하지 않습니다."),
    POST_ID_NOT_FOUND(HttpStatus.BAD_REQUEST,"해당 게시글이 존재하지 않습니다."),
    POST_EDIT_FORBIDDEN(HttpStatus.FORBIDDEN, "게시글 수정 권한이 없습니다."),
    POST_DELETE_FORBIDDEN(HttpStatus.FORBIDDEN, "게시글 삭제 권한이 없습니다."),

    COMMENT_ID_NOT_FOUND(HttpStatus.BAD_REQUEST,"해당 댓글이 존재하지 않습니다."),
    COMMENT_DELETE_FORBIDDEN(HttpStatus.FORBIDDEN,"댓글 삭제 권한이 존재하지 않습니다."),

    REPLY_ID_NOT_FOUND(HttpStatus.BAD_REQUEST,"해당 대댓글이 존재하지 않습니다."),

    POST_NOT_VOTABLE(HttpStatus.BAD_REQUEST,"투표 기능이 존재 하지않습니다."),

    POST_ID_MIXMATCH(HttpStatus.BAD_REQUEST, "게시글 ID가 일치하지 않습니다."),
    COMMENT_ID_MISMATCH(HttpStatus.BAD_REQUEST, "댓글 ID가 일치하지 않습니다."),
    REPLY_DELETE_FORBIDDEN(HttpStatus.FORBIDDEN,"대댓글 삭제 권한이 없습니다."),

    CANNOT_REPORT(HttpStatus.FORBIDDEN, "신고 권한이 없습니다."),
    ALREADY_REPORTED(HttpStatus.FORBIDDEN, "이미 신고가 되었습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
