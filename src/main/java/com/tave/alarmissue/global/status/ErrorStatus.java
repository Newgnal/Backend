package com.tave.alarmissue.global.status;

import com.tave.alarmissue.global.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements ErrorCode {


    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}