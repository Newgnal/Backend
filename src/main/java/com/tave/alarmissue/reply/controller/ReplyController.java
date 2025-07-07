package com.tave.alarmissue.reply.controller;

import com.tave.alarmissue.auth.dto.request.PrincipalUserDetails;
import com.tave.alarmissue.reply.dto.request.ReplyCreateRequestDto;
import com.tave.alarmissue.reply.dto.response.ReplyResponseDto;
import com.tave.alarmissue.reply.service.ReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("/posts/v1/{postId}/comments/{commentId}/replies")
public class ReplyController {

    private final ReplyService replyService;

    @PostMapping
    public ResponseEntity<ReplyResponseDto> createReply(@RequestBody ReplyCreateRequestDto dto, @PathVariable Long postId, @PathVariable Long commentId, @AuthenticationPrincipal PrincipalUserDetails principal){
        Long userId = principal.getUserId();

        ReplyResponseDto responseDto = replyService.createReply(dto,postId,commentId,userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }
    @DeleteMapping("{replyId}")
    public ResponseEntity<Void> deleteReply(@PathVariable Long replyId, @PathVariable Long postId, @PathVariable Long commentId, @AuthenticationPrincipal PrincipalUserDetails principal){
        Long userId = principal.getUserId();

        replyService.deleteReply(replyId,postId,commentId,userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
