package com.tave.alarmissue.global.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class CommonResponse<T> {
    private int status;
    private String message;
    private T data;

    @Builder
    public CommonResponse(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public CommonResponse() {
    }
}
