package com.tave.alarmissue.news.controller;

import com.tave.alarmissue.auth.dto.request.PrincipalUserDetails;
import com.tave.alarmissue.news.dto.request.NewsVoteRequestDto;
import com.tave.alarmissue.news.dto.response.NewsVoteResponseDto;
import com.tave.alarmissue.news.service.NewsVoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/news/v1")
public class NewsVoteController {

    private final NewsVoteService newsvoteService;

    //뉴스 투표
    @PostMapping("/vote")
    public ResponseEntity<NewsVoteResponseDto> createVoteAndGetResult(@RequestBody NewsVoteRequestDto dto,
                                                                      @AuthenticationPrincipal PrincipalUserDetails principal) {
        Long userId = principal.getUserId();
        newsvoteService.createVoteAndGetResult(dto, userId);

        NewsVoteResponseDto result = newsvoteService.getVoteResult(dto, userId);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}



