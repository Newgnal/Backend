package com.tave.alarmissue.global.exception;

import com.tave.alarmissue.global.dto.CommonResponse;
import com.tave.alarmissue.global.status.ErrorStatus;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

@Getter
public class CustomException extends RuntimeException {

    private final ErrorStatus errorStatus;

    public CustomException(ErrorStatus errorStatus) {
        super(errorStatus.getMessage());
        this.errorStatus = errorStatus;
    }
    public ErrorStatus getErrorStatus() {
        return errorStatus;
    }
    public static <T> ResponseEntity<CommonResponse<T>> createErrorResponse(ErrorStatus errorStatus, T data) {
        CommonResponse<T> response = CommonResponse.<T>builder()
                .status(errorStatus.getHttpStatus().value())
                .message(errorStatus.getMessage())
                .data(data)
                .build();

        return ResponseEntity
                .status(errorStatus.getHttpStatus())
                .body(response);
    }
}
