package com.tave.alarmissue.news.controller;

import com.tave.alarmissue.news.dto.response.NewsDetailResponseDto;
import com.tave.alarmissue.news.service.NewsService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class NewsDetailController {
    private final NewsService newsService;

    @Operation(summary = "뉴스 상세 페이지")
    @GetMapping("/news/{newsId}")
    public ResponseEntity<NewsDetailResponseDto> getNews(@PathVariable Long newsId){
        NewsDetailResponseDto response = newsService.getDetailNews(newsId);
        return ResponseEntity.ok(response);
    }
}
