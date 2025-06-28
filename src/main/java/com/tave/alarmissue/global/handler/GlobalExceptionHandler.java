package com.tave.alarmissue.global.handler;

import com.tave.alarmissue.global.dto.ResponseError;
import com.tave.alarmissue.global.exception.CustomException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Builder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
@Builder
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ResponseError> handleCustomException(CustomException e,
                                                               HttpServletRequest request) {

        ResponseError responseError = ResponseError.builder()
                .messageDetail(e.getMessage())
                .errorDetail(e.getErrorCode().getMessage())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(e.getErrorCode().getHttpStatus())
                .body(responseError);

    }

    // 요청 본문이 없거나 변환할 수 없는 경우 (NOT NULL)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ResponseError> handleHttpMessageNotReadableException(HttpMessageNotReadableException e,
                                                                               HttpServletRequest request) {

        ResponseError responseError = ResponseError.builder()
                .messageDetail("요청 본문이 누락되었거나 올바르지 않습니다.")
                .errorDetail(e.getMessage())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseError);
    }

    //Validation 에러 처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseError> handleValidationException(MethodArgumentNotValidException e,
                                                                   HttpServletRequest request) {
        String errorMessage = e.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        ResponseError responseError = ResponseError.builder()
                .messageDetail("입력값 검증에 실패했습니다.")
                .errorDetail(errorMessage)
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseError);
    }
    //-------- 아래 내 코드 ----
    // Bean Validation 오류 처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseError> handleValidationException(MethodArgumentNotValidException e,
                                                                   HttpServletRequest request) {
        String errorMessage = e.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        ResponseError responseError = ResponseError.builder()
                .messageDetail(errorMessage)
                .errorDetail("유효성 검사 실패")
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseError);
    }
}