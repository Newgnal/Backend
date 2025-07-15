package com.tave.alarmissue.post.controller;

import com.tave.alarmissue.auth.dto.request.PrincipalUserDetails;
import com.tave.alarmissue.post.dto.response.ReportResponse;
import com.tave.alarmissue.post.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/post/v1")
@Tag(name = "커뮤니티 신고 API")
public class ReportController {
    private final ReportService reportService;

    @PatchMapping("/{postId}/report")
    @Operation(summary = "게시글 신고", description = "해당 게시글 신고입니다.")
    public ResponseEntity<ReportResponse> createPostReport(@AuthenticationPrincipal PrincipalUserDetails principal,
                                                           @PathVariable Long postId) {
        Long userId = principal.getUserId();

        ReportResponse responseDto = reportService.createPostReport(userId,postId);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);

    }
    @PatchMapping("comment/{commentId}/report")
    @Operation(summary = "댓글 신고", description = "해당 댓글 신고입니다.")
    public ResponseEntity<ReportResponse> createCommentReport( @AuthenticationPrincipal PrincipalUserDetails principal,
                                                               @PathVariable Long commentId) {
        Long userId = principal.getUserId();

        ReportResponse responseDto = reportService.createCommentReport(userId,commentId);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);

    }
    @PatchMapping("reply/{replyId}/report")
    @Operation(summary = "대댓글 신고", description = "해당 대댓글 신고입니다.")
    public ResponseEntity<ReportResponse> createReplyReport( @AuthenticationPrincipal PrincipalUserDetails principal,
                                                               @PathVariable Long replyId){

        Long userId = principal.getUserId();

        ReportResponse responseDto = reportService.createReplyReport(userId,replyId);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);

    }
}
