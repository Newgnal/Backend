package com.tave.alarmissue.inquiry.service;

import com.tave.alarmissue.inquiry.converter.InquiryConverter;
import com.tave.alarmissue.inquiry.domain.Inquiry;
import com.tave.alarmissue.inquiry.dto.request.InquiryRequest;
import com.tave.alarmissue.inquiry.dto.response.InquiryResponse;
import com.tave.alarmissue.inquiry.repository.InquiryRepository;
import com.tave.alarmissue.user.domain.UserEntity;
import com.tave.alarmissue.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class InquiryService {

    private final UserRepository userRepository;
    private final InquiryRepository inquiryRepository;

    @Transactional
    public InquiryResponse postInquiry(Long userId, InquiryRequest inquiryRequest) {

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("ID: " + userId));


        Inquiry inquiry = Inquiry.createFrom(inquiryRequest, user);
        Inquiry saved = inquiryRepository.save(inquiry);

        return InquiryConverter.toInquiryResponse(saved);
    }
}
