package com.tave.alarmissue.post.controller;

import com.tave.alarmissue.auth.dto.request.PrincipalUserDetails;
import com.tave.alarmissue.post.dto.request.CommentCreateRequestDto;
import com.tave.alarmissue.post.dto.response.CommentResponseDto;
import com.tave.alarmissue.post.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/posts/v1/{postId}/comments")
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentResponseDto> createComment(@RequestBody CommentCreateRequestDto dto, @AuthenticationPrincipal PrincipalUserDetails principal, @PathVariable Long postId) {
        Long userId = principal.getUserId();

        CommentResponseDto responseDto = commentService.createComment(dto,userId,postId);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);}

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId, @AuthenticationPrincipal PrincipalUserDetails principal, @PathVariable Long postId) {
        Long userId = principal.getUserId();

        commentService.deleteComment(commentId,userId,postId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }


}

