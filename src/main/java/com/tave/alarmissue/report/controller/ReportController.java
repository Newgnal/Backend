package com.tave.alarmissue.report.controller;

import com.tave.alarmissue.auth.dto.request.PrincipalUserDetails;
import com.tave.alarmissue.report.dto.requestdto.ReportCreateRequestDto;
import com.tave.alarmissue.report.dto.responsedto.ReportResponseDto;
import com.tave.alarmissue.report.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/posts/v1/{postId}/reports")
public class ReportController {
    private final ReportService reportService;

    @PostMapping
    public ResponseEntity<ReportResponseDto> createPostReport(@RequestBody ReportCreateRequestDto dto, @AuthenticationPrincipal PrincipalUserDetails principal, @PathVariable Long postId) {
        Long userId = principal.getUserId();

        ReportResponseDto responseDto = reportService.createPostReport(dto,userId,postId);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);

    }
    @PostMapping("{commentId}")
    public ResponseEntity<ReportResponseDto> createCommentReport(@RequestBody ReportCreateRequestDto dto, @AuthenticationPrincipal PrincipalUserDetails principal, @PathVariable Long postId, @PathVariable Long commentId) {
        Long userId = principal.getUserId();

        ReportResponseDto responseDto = reportService.createCommentReport(dto,userId,postId,commentId);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);

    }

}
