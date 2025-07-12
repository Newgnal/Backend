package com.tave.alarmissue.post.controller;

import com.tave.alarmissue.auth.dto.request.PrincipalUserDetails;
import com.tave.alarmissue.post.dto.request.CommentCreateRequest;
import com.tave.alarmissue.post.dto.request.ReplyCreateRequest;
import com.tave.alarmissue.post.dto.response.CommentResponse;
import com.tave.alarmissue.post.dto.response.ReplyResponse;
import com.tave.alarmissue.post.service.CommentService;
import com.tave.alarmissue.post.service.ReplyService;
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
    private final ReplyService replyService;

    // 댓글 작성
    @PostMapping("{postId}")
    public ResponseEntity<CommentResponse> createComment(@RequestBody CommentCreateRequest dto,
                                                         @AuthenticationPrincipal PrincipalUserDetails principal,
                                                         @PathVariable Long postId) {
        Long userId = principal.getUserId();

        CommentResponse responseDto = commentService.createComment(dto,userId,postId);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);}

    // 댓글 삭제
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId,
                                              @AuthenticationPrincipal PrincipalUserDetails principal) {
        Long userId = principal.getUserId();

        commentService.deleteComment(commentId,userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }

    // 대댓글 작성
    @PostMapping("reply/{commentId}")
    public ResponseEntity<ReplyResponse> createReply(@RequestBody ReplyCreateRequest dto,
                                                     @PathVariable Long commentId,
                                                     @AuthenticationPrincipal PrincipalUserDetails principal){
        Long userId = principal.getUserId();

        ReplyResponse responseDto = replyService.createReply(dto,commentId,userId);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    // 대댓글 삭제
    @DeleteMapping("{replyId}")
    public ResponseEntity<Void> deleteReply(@PathVariable Long replyId ,
                                            @AuthenticationPrincipal PrincipalUserDetails principal){

        Long userId = principal.getUserId();

        replyService.deleteReply(replyId,userId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}

