package com.tave.alarmissue.inquiry.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InquiryResponse {

    private Long userId;
    private String email;
    private String title;
    private String body;
}
