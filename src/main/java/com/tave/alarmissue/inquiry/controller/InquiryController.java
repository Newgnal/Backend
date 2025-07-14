package com.tave.alarmissue.inquiry.controller;

import com.tave.alarmissue.auth.dto.request.PrincipalUserDetails;
import com.tave.alarmissue.inquiry.dto.request.InquiryRequest;
import com.tave.alarmissue.inquiry.dto.response.InquiryResponse;
import com.tave.alarmissue.inquiry.service.InquiryService;
import com.tave.alarmissue.news.dto.response.NewsResponseDto;
import com.tave.alarmissue.post.dto.response.LikeResponse;
import com.tave.alarmissue.search.dto.SearchListResponse;
import com.tave.alarmissue.search.dto.SearchResultResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/inquiry/v1")
public class InquiryController {

    private final InquiryService inquiryService;

    @PostMapping("")
    public ResponseEntity<InquiryResponse> searchNews(@AuthenticationPrincipal PrincipalUserDetails principal,
                                                      @RequestBody InquiryRequest inquiryRequest) {

        Long userId = principal.getUserId();

        InquiryResponse responseDto = inquiryService.postInquiry(userId,inquiryRequest);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);

    }

}
