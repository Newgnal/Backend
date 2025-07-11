package com.tave.alarmissue.post.controller;

import com.tave.alarmissue.auth.dto.request.PrincipalUserDetails;
import com.tave.alarmissue.post.dto.request.ReportCreateRequestDto;
import com.tave.alarmissue.post.dto.response.ReportResponseDto;
import com.tave.alarmissue.post.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/posts/v1")
public class ReportController {
    private final ReportService reportService;

    @PostMapping("/{postId}/reports")
    public ResponseEntity<ReportResponseDto> createPostReport(@RequestBody ReportCreateRequestDto dto, @AuthenticationPrincipal PrincipalUserDetails principal, @PathVariable Long postId) {
        Long userId = principal.getUserId();

        ReportResponseDto responseDto = reportService.createPostReport(dto,userId,postId);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);

    }
    @PostMapping("comments/{commentId}/reports")
    public ResponseEntity<ReportResponseDto> createCommentReport(@RequestBody ReportCreateRequestDto dto, @AuthenticationPrincipal PrincipalUserDetails principal, @PathVariable Long commentId) {
        Long userId = principal.getUserId();

        ReportResponseDto responseDto = reportService.createCommentReport(dto,userId,commentId);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);

    }

}
