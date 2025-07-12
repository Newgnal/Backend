package com.tave.alarmissue.post.controller;

import com.tave.alarmissue.auth.dto.request.PrincipalUserDetails;
import com.tave.alarmissue.post.dto.request.CommentCreateRequest;
import com.tave.alarmissue.post.dto.response.CommentResponse;
import com.tave.alarmissue.post.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/post/v1/comment")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("{postId}")
    public ResponseEntity<CommentResponse> createComment(@RequestBody CommentCreateRequest dto,
                                                         @AuthenticationPrincipal PrincipalUserDetails principal,
                                                         @PathVariable Long postId) {
        Long userId = principal.getUserId();

        CommentResponse responseDto = commentService.createComment(dto,userId,postId);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);}

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId,
                                              @AuthenticationPrincipal PrincipalUserDetails principal) {
        Long userId = principal.getUserId();

        commentService.deleteComment(commentId,userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }


}

