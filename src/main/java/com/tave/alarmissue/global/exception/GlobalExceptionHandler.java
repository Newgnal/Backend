package com.tave.alarmissue.global.exception;


import com.tave.alarmissue.fcm.exception.FcmErrorCode;
import com.tave.alarmissue.fcm.exception.FcmException;
import com.tave.alarmissue.global.dto.CommonResponse;
import com.tave.alarmissue.global.dto.ResponseError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(FcmException.class)
    public ResponseEntity<CommonResponse<ResponseError>> handleFcmException(FcmException ex) {
        FcmErrorCode errorCode = ex.getErrorCode();

        ResponseError error = new ResponseError();
        error.setPath("/api/notification");
        error.setMessageDetail(errorCode.getMessage());
        error.setErrorDetail(errorCode.name());

        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(CommonResponse.<ResponseError>builder()
                        .status(errorCode.getHttpStatus().value())
                        .message(errorCode.getMessage())
                        .data(error)
                        .build());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<CommonResponse<ResponseError>> handleIllegalArgument(IllegalArgumentException ex) {
        ResponseError error = new ResponseError();
        error.setPath("/api/notification");
        error.setMessageDetail("잘못된 요청입니다.");
        error.setErrorDetail(ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(CommonResponse.<ResponseError>builder()
                        .status(400)
                        .message("잘못된 요청입니다.")
                        .data(error)
                        .build());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CommonResponse<ResponseError>> handleGeneral(Exception ex) {
        ResponseError error = new ResponseError();
        error.setPath("/api/notification");
        error.setMessageDetail("서버 오류가 발생했습니다.");
        error.setErrorDetail(ex.getMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(CommonResponse.<ResponseError>builder()
                        .status(500)
                        .message("서버 오류가 발생했습니다.")
                        .data(error)
                        .build());
    }
}
