package com.tave.alarmissue.news.controller;

import com.tave.alarmissue.auth.dto.request.PrincipalUserDetails;
import com.tave.alarmissue.news.dto.request.NewsVoteRequestDto;
import com.tave.alarmissue.news.dto.response.NewsVoteResponseDto;
import com.tave.alarmissue.news.service.NewsVoteService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "뉴스 투표 API")
public class NewsVoteController {
    private final NewsVoteService newsvoteService;

    @PostMapping("/news/v1/vote") //투표
    public ResponseEntity<NewsVoteResponseDto> createVoteAndGetResult(@RequestBody NewsVoteRequestDto dto,
                                                                                      @AuthenticationPrincipal PrincipalUserDetails principal) {
        Long userId = principal.getUserId();
        NewsVoteResponseDto newsVoteResponseDto = newsvoteService.createVoteAndGetResult(dto, userId);

        return ResponseEntity.status(HttpStatus.CREATED).body(newsVoteResponseDto);
    }
}



