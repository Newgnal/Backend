package com.tave.alarmissue.post.controller;

import com.tave.alarmissue.auth.dto.request.PrincipalUserDetails;
import com.tave.alarmissue.post.dto.request.VoteRequest;
import com.tave.alarmissue.post.dto.response.VoteResponse;
import com.tave.alarmissue.post.service.VoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vote/v1")
@RequiredArgsConstructor
public class VoteController {

    private final VoteService voteService;

    // 투표
    @PostMapping
    public ResponseEntity<VoteResponse> createVoteAndGetResult(@RequestBody VoteRequest dto,
                                                               @AuthenticationPrincipal PrincipalUserDetails principal) {

        Long userId = principal.getUserId();
        VoteResponse voteResponseDto = voteService.createVoteAndGetResult(dto, userId);

        return ResponseEntity.status(HttpStatus.OK).body(voteResponseDto);
    }

}