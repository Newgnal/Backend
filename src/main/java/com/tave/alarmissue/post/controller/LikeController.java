package com.tave.alarmissue.post.controller;

import com.tave.alarmissue.auth.dto.request.PrincipalUserDetails;
import com.tave.alarmissue.post.dto.response.LikeResponseDto;
import com.tave.alarmissue.post.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("/posts/v1")
public class LikeController {

    private final LikeService likeService;
    //게시글 좋아요
    @PatchMapping("/{postId}/likes")
    public ResponseEntity<LikeResponseDto> postLike(@AuthenticationPrincipal PrincipalUserDetails principal,@PathVariable Long postId) {

        Long userId = principal.getUserId();

        LikeResponseDto responseDto = likeService.postLike(userId,postId);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);

    }
    @PatchMapping("/comments/{commentId}/likes")
    public ResponseEntity<LikeResponseDto> commentLike(@AuthenticationPrincipal PrincipalUserDetails principal,@PathVariable Long commentId) {

        Long userId = principal.getUserId();

        LikeResponseDto responseDto = likeService.commentLike(userId,commentId);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @PatchMapping("/replies/{replyId}/likes")
    public ResponseEntity<LikeResponseDto> replyLike(@AuthenticationPrincipal PrincipalUserDetails principal,@PathVariable Long replyId) {
        Long userId = principal.getUserId();

        LikeResponseDto responseDto = likeService.replyLike(userId,replyId);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}
