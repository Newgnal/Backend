package com.tave.alarmissue.news.poll.controller;

import com.tave.alarmissue.auth.dto.request.PrincipalUserDetails;
import com.tave.alarmissue.news.poll.dto.request.NewsVoteRequestDto;
import com.tave.alarmissue.news.poll.dto.response.NewsVoteResponseDto;
import com.tave.alarmissue.news.poll.service.NewsVoteService;
import com.tave.alarmissue.vote.dto.request.VoteRequestDto;
import com.tave.alarmissue.vote.dto.response.VoteResponseDto;
import com.tave.alarmissue.vote.service.VoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class NewsVoteController {
    private final NewsVoteService newsvoteService;

    @PostMapping("/news/v1/{newsId}/vote") //투표
    public ResponseEntity<NewsVoteResponseDto> createVoteAndGetResult(@RequestBody NewsVoteRequestDto dto,
                                                                                      @AuthenticationPrincipal PrincipalUserDetails principal) {
        Long userId = principal.getUserId();
        NewsVoteResponseDto newsVoteResponseDto = newsvoteService.createVoteAndGetResult(dto, userId);

        return ResponseEntity.status(HttpStatus.CREATED).body(newsVoteResponseDto);
    }
}



