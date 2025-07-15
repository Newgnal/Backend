package com.tave.alarmissue.inquiry.dto.request;

import lombok.Data;

@Data
public class InquiryRequest {

    private String email;
    private String title;
    private String body;

}
