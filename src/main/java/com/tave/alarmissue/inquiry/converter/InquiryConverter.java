package com.tave.alarmissue.inquiry.converter;

import com.tave.alarmissue.inquiry.domain.Inquiry;
import com.tave.alarmissue.inquiry.dto.response.InquiryResponse;

public class InquiryConverter {

    public static InquiryResponse toInquiryResponse(Inquiry inquiry) {
        return InquiryResponse.builder()
                .userId(inquiry.getUser().getId())
                .email(inquiry.getEmail())
                .title(inquiry.getTitle())
                .body(inquiry.getContent())
                .build();
    }

}
