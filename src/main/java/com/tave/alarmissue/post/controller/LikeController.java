package com.tave.alarmissue.post.controller;

import com.tave.alarmissue.auth.dto.request.PrincipalUserDetails;
import com.tave.alarmissue.post.dto.response.LikeResponse;
import com.tave.alarmissue.post.service.LikeService;
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
    public ResponseEntity<LikeResponse> postLike(@AuthenticationPrincipal PrincipalUserDetails principal,
                                                 @PathVariable Long postId) {

        Long userId = principal.getUserId();

        LikeResponse responseDto = likeService.postLike(userId,postId);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);

    }

    @PatchMapping("comment/{commentId}/likes")
    public ResponseEntity<LikeResponse> commentLike(@AuthenticationPrincipal PrincipalUserDetails principal,
                                                    @PathVariable Long commentId) {

        Long userId = principal.getUserId();

        LikeResponse responseDto = likeService.commentLike(userId,commentId);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @PatchMapping("reply/{replyId}/likes")
    public ResponseEntity<LikeResponse> replyLike(@AuthenticationPrincipal PrincipalUserDetails principal,
                                                  @PathVariable Long replyId) {

        Long userId = principal.getUserId();

        LikeResponse responseDto = likeService.replyLike(userId,replyId);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}
