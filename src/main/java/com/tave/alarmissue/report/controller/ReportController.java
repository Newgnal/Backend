package com.tave.alarmissue.report.controller;

import com.tave.alarmissue.auth.dto.request.PrincipalUserDetails;
import com.tave.alarmissue.report.dto.response.ReportResponse;
import com.tave.alarmissue.report.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/report/v1")
@Tag(name = "신고 API")
public class ReportController {
    private final ReportService reportService;

    @PatchMapping("/{postId}")
    @Operation(summary = "커뮤니티 게시글 신고", description = "해당 게시글 신고입니다.")
    public ResponseEntity<ReportResponse> createPostReport(@AuthenticationPrincipal PrincipalUserDetails principal,
                                                           @PathVariable Long postId) {
        Long userId = principal.getUserId();

        ReportResponse responseDto = reportService.createPostReport(userId,postId);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);

    }
    @PatchMapping("comment/{commentId}")
    @Operation(summary = "커뮤니티 댓글 신고", description = "해당 댓글 신고입니다.")
    public ResponseEntity<ReportResponse> createCommentReport( @AuthenticationPrincipal PrincipalUserDetails principal,
                                                               @PathVariable Long commentId) {
        Long userId = principal.getUserId();

        ReportResponse responseDto = reportService.createCommentReport(userId,commentId);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);

    }
    @PatchMapping("reply/{replyId}")
    @Operation(summary = "커뮤니티 대댓글 신고", description = "해당 대댓글 신고입니다.")
    public ResponseEntity<ReportResponse> createReplyReport( @AuthenticationPrincipal PrincipalUserDetails principal,
                                                               @PathVariable Long replyId){

        Long userId = principal.getUserId();

        ReportResponse responseDto = reportService.createReplyReport(userId,replyId);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);

    }

    @PatchMapping("news_comment/{newsCommentId}")
    @Operation(summary = "뉴스 댓글 신고", description = "해당 뉴스 댓글 신고입니다.")
    public ResponseEntity<ReportResponse> createNewsCommentReport( @AuthenticationPrincipal PrincipalUserDetails principal,
                                                             @PathVariable Long newsCommentId){

        Long userId = principal.getUserId();

        ReportResponse responseDto = reportService.createNewsCommentReport(userId,newsCommentId);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);

    }
}
