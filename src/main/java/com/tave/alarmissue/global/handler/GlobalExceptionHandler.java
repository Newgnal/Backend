package com.tave.alarmissue.global.handler;

import com.tave.alarmissue.global.dto.ResponseError;
import com.tave.alarmissue.global.exception.CustomException;
import com.tave.alarmissue.news.exceptions.NewsErrorCode;
import com.tave.alarmissue.news.exceptions.NewsException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.Builder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

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

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ResponseError> handleConstraintViolationException(ConstraintViolationException e,
                                                                            HttpServletRequest request) {
        String errorMessage = e.getConstraintViolations().stream()
                .map(violation -> violation.getMessage())
                .collect(Collectors.joining(", "));

        ResponseError responseError = ResponseError.builder()
                .messageDetail(errorMessage)
                .errorDetail("파라미터 유효성 검사 실패")
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseError);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ResponseError> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e,
                                                                                   HttpServletRequest request) {
        if (e.getName().contains("Date")) {
            throw new NewsException(NewsErrorCode.INVALID_DATE_FORMAT,
                    "날짜 형식이 올바르지 않습니다. yyyy-MM-dd 형식으로 입력해주세요.");
        }

        ResponseError responseError = ResponseError.builder()
                .messageDetail("파라미터 타입이 올바르지 않습니다: " + e.getName())
                .errorDetail("타입 변환 오류")
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseError);
    }

}