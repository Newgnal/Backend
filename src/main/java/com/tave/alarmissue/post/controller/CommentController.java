package com.tave.alarmissue.post.controller;

import com.tave.alarmissue.auth.dto.request.PrincipalUserDetails;
import com.tave.alarmissue.post.dto.request.CommentCreateRequest;
import com.tave.alarmissue.post.dto.request.ReplyCreateRequest;
import com.tave.alarmissue.post.dto.response.CommentResponse;
import com.tave.alarmissue.post.dto.response.ReplyResponse;
import com.tave.alarmissue.post.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/post/v1/comment")
@Tag(name = "커뮤니티 댓글 API")
public class CommentController {

    private final CommentService commentService;

    // 댓글 작성
    @PostMapping("{postId}")
    @Operation(summary = "댓글 작성", description = "해당 게시글에 댓글 작성합니다.")
    public ResponseEntity<CommentResponse> createComment(@RequestBody CommentCreateRequest dto,
                                                         @AuthenticationPrincipal PrincipalUserDetails principal,
                                                         @PathVariable Long postId) {
        Long userId = principal.getUserId();

        CommentResponse responseDto = commentService.createComment(dto,userId,postId);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);}

    // 댓글 삭제
    @DeleteMapping("/{commentId}")
    @Operation(summary = "댓글 삭제", description = "해당 댓글을 삭제합니다.")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId,
                                              @AuthenticationPrincipal PrincipalUserDetails principal) {
        Long userId = principal.getUserId();

        commentService.deleteComment(commentId,userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }

    // 대댓글 작성
    @PostMapping("reply/{commentId}")
    @Operation(summary = "대댓글 작성", description = "해당 댓글에 대댓글을 작성합니다.")
    public ResponseEntity<ReplyResponse> createReply(@RequestBody ReplyCreateRequest dto,
                                                     @PathVariable Long commentId,
                                                     @AuthenticationPrincipal PrincipalUserDetails principal){
        Long userId = principal.getUserId();

        ReplyResponse responseDto = commentService.createReply(dto,commentId,userId);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    // 대댓글 삭제
    @DeleteMapping("reply/{replyId}")
    @Operation(summary = "대댓글 삭제", description = "해당 대댓글을 삭제합니다.")
    public ResponseEntity<Void> deleteReply(@PathVariable Long replyId ,
                                            @AuthenticationPrincipal PrincipalUserDetails principal){

        Long userId = principal.getUserId();

        commentService.deleteReply(replyId,userId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}

