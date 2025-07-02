package com.tave.alarmissue.comment.controller;

import com.tave.alarmissue.auth.dto.request.PrincipalUserDetails;
import com.tave.alarmissue.comment.dto.request.CommentCreateRequestDto;
import com.tave.alarmissue.comment.dto.response.CommentResponseDto;
import com.tave.alarmissue.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;



@RequiredArgsConstructor
@RestController
@RequestMapping("/posts/v1/{postId}/comments")
public class CommentController {

    private final CommentService commentService;

    @GetMapping
    public ResponseEntity<Page<CommentResponseDto>> getComments(
            @PathVariable Long postId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<CommentResponseDto> comments = commentService.getComment(postId, pageable);
        return ResponseEntity.ok(comments);
    }

    @PostMapping
    public ResponseEntity<CommentResponseDto> createComment(@RequestBody CommentCreateRequestDto dto, @AuthenticationPrincipal PrincipalUserDetails principal, @PathVariable Long postId) {
        Long userId = principal.getUserId();

        CommentResponseDto responseDto = commentService.createComment(dto,userId,postId);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }


    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId, @AuthenticationPrincipal PrincipalUserDetails principal, @PathVariable Long postId) {
        Long userId = principal.getUserId();

        commentService.deleteComment(commentId,userId,postId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }
}
