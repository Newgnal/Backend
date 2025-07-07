package com.tave.alarmissue.news.controller;

import com.tave.alarmissue.auth.dto.request.PrincipalUserDetails;
import com.tave.alarmissue.news.dto.request.NewsCommentRequestDto;
import com.tave.alarmissue.news.dto.response.NewsCommentResponseDto;
import com.tave.alarmissue.news.service.NewsCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/news/v1/{newsId}/comments")
public class NewsCommentController {

    private final NewsCommentService newsCommentService;

    @PostMapping
    public ResponseEntity<NewsCommentResponseDto> createComment(@RequestBody NewsCommentRequestDto dto, @AuthenticationPrincipal PrincipalUserDetails principal, @PathVariable Long newsId) {
        Long userId = principal.getUserId();
        NewsCommentResponseDto responseDto = newsCommentService.createComment(dto,userId,newsId);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }
}
