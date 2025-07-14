package com.tave.alarmissue.news.controller;

import com.tave.alarmissue.news.domain.enums.Thema;
import com.tave.alarmissue.news.dto.request.enums.NewsSortType;
import com.tave.alarmissue.news.dto.response.NewsDetailResponseDto;
import com.tave.alarmissue.news.dto.response.NewsResponseDto;
import com.tave.alarmissue.news.dto.response.SliceResponseDto;
import com.tave.alarmissue.news.service.NewsService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/news/v1")
public class NewsController {

    private final NewsService newsService;

    @Operation(summary = "뉴스 전체 조회")
    @GetMapping("")
    public ResponseEntity<SliceResponseDto<NewsResponseDto>> getAllNews(@RequestParam(defaultValue = "LATEST")NewsSortType sortType,
                                                                        @RequestParam(defaultValue = "0") int page,
                                                                        @RequestParam(defaultValue = "5") int size) {
        Pageable pageable= PageRequest.of(page,size);

        SliceResponseDto<NewsResponseDto> response = newsService.getAllNews(sortType,pageable);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }



    @Operation(summary = "테마별 뉴스 전체 조회")
    @GetMapping("/thema/{thema}")   //그냥 /{thema}로만 하니까 경로 오류 발생해서!
    public ResponseEntity<SliceResponseDto<NewsResponseDto>> getAllThemaNews(@RequestParam(defaultValue = "LATEST")NewsSortType sortType,
                                                                             @RequestParam(defaultValue = "0")int page,
                                                                             @RequestParam(defaultValue = "5") int size,
                                                                             @PathVariable Thema thema) {
        Pageable pageable= PageRequest.of(page,size);
        SliceResponseDto<NewsResponseDto> response = newsService.getAllThemaNews(sortType,thema,pageable);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @Operation(summary = "뉴스 상세 페이지")
    @GetMapping("/{newsId}")
    public ResponseEntity<NewsDetailResponseDto> getNews(@PathVariable Long newsId){
        NewsDetailResponseDto response = newsService.getDetailNews(newsId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }



}
