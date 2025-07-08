package com.tave.alarmissue.report.exception;

import com.tave.alarmissue.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
@RequiredArgsConstructor
@Getter
public enum ReportErrorCode implements ErrorCode {
    USER_ID_NOT_FOUND(HttpStatus.BAD_REQUEST, "해당 사용자가 존재하지 않습니다."),
    POST_ID_NOT_FOUND(HttpStatus.BAD_REQUEST,"해당 게시글이 존재하지 않습니다."),
    COMMENT_ID_NOT_FOUND(HttpStatus.BAD_REQUEST,"해당 댓글이 존재하지 않습니다."),
    POST_REPORT_FORBIDDEN(HttpStatus.FORBIDDEN, "게시글 신고 권한이 없습니다."),
    COMMENT_REPORT_FORBIDDEN(HttpStatus.FORBIDDEN, "댓글 삭제 권한이 없습니다."),
    POST_ID_MIXMATCH(HttpStatus.BAD_REQUEST,"댓글과 게시글의 아이디가 다릅니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
