package com.tave.alarmissue.like.controller;

import com.tave.alarmissue.auth.dto.request.PrincipalUserDetails;
import com.tave.alarmissue.like.dto.request.LikeCreateRequestDto;
import com.tave.alarmissue.like.dto.response.LikeResponseDto;
import com.tave.alarmissue.like.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("/posts/v1/{postId}/likes")
public class LikeController {

    private final LikeService likeService;
    //게시글 좋아요
    @PostMapping()
    public ResponseEntity<LikeResponseDto> postLike(@RequestBody LikeCreateRequestDto dto, @AuthenticationPrincipal PrincipalUserDetails principal,@PathVariable Long postId) {

        Long userId = principal.getUserId();

        LikeResponseDto responseDto = likeService.postLike(dto,userId,postId);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);

    }
    @PostMapping("{commentId}")
    public ResponseEntity<LikeResponseDto> commentLike(@RequestBody LikeCreateRequestDto dto, @AuthenticationPrincipal PrincipalUserDetails principal,@PathVariable Long postId,@PathVariable Long commentId) {

        Long userId = principal.getUserId();

        LikeResponseDto responseDto = likeService.commentLike(dto,userId,postId,commentId);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);

    }
}
