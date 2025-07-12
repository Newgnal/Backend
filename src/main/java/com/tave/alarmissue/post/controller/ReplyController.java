package com.tave.alarmissue.post.controller;

import com.tave.alarmissue.auth.dto.request.PrincipalUserDetails;
import com.tave.alarmissue.post.dto.request.ReplyCreateRequest;
import com.tave.alarmissue.post.dto.response.ReplyResponse;
import com.tave.alarmissue.post.service.ReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("/posts/v1/reply")
public class ReplyController {

    private final ReplyService replyService;

    @PostMapping("/{commentId}")
    public ResponseEntity<ReplyResponse> createReply(@RequestBody ReplyCreateRequest dto,
                                                     @PathVariable Long commentId,
                                                     @AuthenticationPrincipal PrincipalUserDetails principal){
        Long userId = principal.getUserId();

        ReplyResponse responseDto = replyService.createReply(dto,commentId,userId);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @DeleteMapping("{replyId}")
    public ResponseEntity<Void> deleteReply(@PathVariable Long replyId ,
                                            @AuthenticationPrincipal PrincipalUserDetails principal){

        Long userId = principal.getUserId();

        replyService.deleteReply(replyId,userId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
