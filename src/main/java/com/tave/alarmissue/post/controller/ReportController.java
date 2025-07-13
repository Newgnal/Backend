package com.tave.alarmissue.post.controller;

import com.tave.alarmissue.auth.dto.request.PrincipalUserDetails;
import com.tave.alarmissue.post.dto.response.ReportResponse;
import com.tave.alarmissue.post.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/post/v1")
public class ReportController {
    private final ReportService reportService;

    @PatchMapping("/{postId}/report")
    public ResponseEntity<ReportResponse> createPostReport(@AuthenticationPrincipal PrincipalUserDetails principal,
                                                           @PathVariable Long postId) {
        Long userId = principal.getUserId();

        ReportResponse responseDto = reportService.createPostReport(userId,postId);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);

    }
    @PatchMapping("comment/{commentId}/report")
    public ResponseEntity<ReportResponse> createCommentReport( @AuthenticationPrincipal PrincipalUserDetails principal,
                                                               @PathVariable Long commentId) {
        Long userId = principal.getUserId();

        ReportResponse responseDto = reportService.createCommentReport(userId,commentId);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);

    }
    @PatchMapping("reply/{replyId}/report")
    public ResponseEntity<ReportResponse> createReplyReport( @AuthenticationPrincipal PrincipalUserDetails principal,
                                                               @PathVariable Long replyId){

        Long userId = principal.getUserId();

        ReportResponse responseDto = reportService.createReplyReport(userId,replyId);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);

    }
}
