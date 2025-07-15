package com.tave.alarmissue.post.controller;

import com.tave.alarmissue.auth.dto.request.PrincipalUserDetails;
import com.tave.alarmissue.post.dto.response.LikeResponse;
import com.tave.alarmissue.post.service.LikeService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("/post/v1")
public class LikeController {

    private final LikeService likeService;

    //게시글 좋아요
    @PatchMapping("/{postId}/likes")
    @Operation(summary = "게시글 좋아요", description = "해당 게시글 좋아요입니다.")
    public ResponseEntity<LikeResponse> postLike(@AuthenticationPrincipal PrincipalUserDetails principal,
                                                 @PathVariable Long postId) {

        Long userId = principal.getUserId();

        LikeResponse responseDto = likeService.postLike(userId,postId);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);

    }

    @PatchMapping("comment/{commentId}/likes")
    @Operation(summary = "댓글 좋아요", description = "해당 댓글 좋아요입니다.")
    public ResponseEntity<LikeResponse> commentLike(@AuthenticationPrincipal PrincipalUserDetails principal,
                                                    @PathVariable Long commentId) {

        Long userId = principal.getUserId();

        LikeResponse responseDto = likeService.commentLike(userId,commentId);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @PatchMapping("reply/{replyId}/likes")
    @Operation(summary = "대댓글 좋아요", description = "해당 대댓글 좋아요입니다.")
    public ResponseEntity<LikeResponse> replyLike(@AuthenticationPrincipal PrincipalUserDetails principal,
                                                  @PathVariable Long replyId) {

        Long userId = principal.getUserId();

        LikeResponse responseDto = likeService.replyLike(userId,replyId);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}
