package com.tave.alarmissue.report.exception;

import com.tave.alarmissue.global.exception.CustomException;
import com.tave.alarmissue.global.exception.ErrorCode;

public class ReportException extends CustomException {
    public ReportException(ErrorCode errorCode) {
        super(errorCode);
    }

    public ReportException(ErrorCode errorCode,String message) {
        super(errorCode,message);
    }
}
