package com.tave.alarmissue.vote.controller;

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
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts/v1")
@RequiredArgsConstructor
public class VoteController {

    private final VoteService voteService;
    private final UserRepository userRepository;

    @PostMapping("/vote") //투표
    public ResponseEntity<Void> vote(@RequestBody VoteRequestDto dto,
                                     @AuthenticationPrincipal User currentUser) {
        Long userId = Long.parseLong(currentUser.getUsername());
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new VoteException(VoteErrorCode.USER_ID_NOT_FOUND));

        voteService.vote(user, dto.getPostId(), dto.getVoteType());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/vote/{postId}") //투표수 조회
    public ResponseEntity<VoteResponseDto> getVoteCounts(@PathVariable Long postId,  @AuthenticationPrincipal User currentUser) {
        Long userId = Long.parseLong(currentUser.getUsername());
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new VoteException(VoteErrorCode.USER_ID_NOT_FOUND));
        VoteResponseDto dto=voteService.getVoteCounts(postId,user);
        return ResponseEntity.ok(dto);
    }
}