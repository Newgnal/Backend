package com.tave.alarmissue.vote.controller;

import com.tave.alarmissue.auth.dto.request.PrincipalUserDetails;
import com.tave.alarmissue.post.exception.PostErrorCode;
import com.tave.alarmissue.post.exception.PostException;
import com.tave.alarmissue.user.domain.UserEntity;
import com.tave.alarmissue.user.repository.UserRepository;
import com.tave.alarmissue.vote.dto.request.VoteRequestDto;
import com.tave.alarmissue.vote.dto.response.VoteResponseDto;
import com.tave.alarmissue.vote.exception.VoteErrorCode;
import com.tave.alarmissue.vote.exception.VoteException;
import com.tave.alarmissue.vote.service.VoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vote/v1")
@RequiredArgsConstructor
public class VoteController {

    private final VoteService voteService;

    @PostMapping //투표
    public ResponseEntity<VoteResponseDto> createVoteAndGetResult(@RequestBody VoteRequestDto dto,
                                                              @AuthenticationPrincipal PrincipalUserDetails principal) {
        Long userId = principal.getUserId();
        VoteResponseDto voteResponseDto = voteService.createVoteAndGetResult(dto, userId);

        return ResponseEntity.status(HttpStatus.CREATED).body(voteResponseDto);
    }

}