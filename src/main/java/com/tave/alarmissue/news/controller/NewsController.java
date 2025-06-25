package com.tave.alarmissue.news.controller;

import com.tave.alarmissue.news.domain.News;
import com.tave.alarmissue.news.domain.enums.Thema;

import com.tave.alarmissue.news.repository.NewsMapping;
import com.tave.alarmissue.news.service.NewsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class NewsController {

    @Autowired
    private final NewsService newsService;


    @GetMapping("/home")
    public List<NewsMapping> getAllNews(){
        return newsService.getAllNews();

    }

    @GetMapping("/home/news/{thema}")
    public List<NewsMapping>getThemaNews(@PathVariable Thema thema){
        return newsService.getThemaNews(thema);
    }

    // 전체 뉴스
//    @GetMapping("/home/news")
//    public ResponseEntity<Page<News>> getAllNews(
//            @RequestParam(defaultValue = "latest") String sort,
//            @PageableDefault(size = 10) Pageable pageable) {
//
//        return ResponseEntity.ok(newsService.getNewsList(sort, pageable));
//    }
//
//    @GetMapping("/home/news/{newsId}")
//    public ResponseEntity<NewsDetailResponseDto> getNewsDetail(@PathVariable Long newsId) {
//        News news = newsService.getNewsById(newsId);
//        return ResponseEntity.ok(NewsDetailResponseDto.fromEntity(news));
//    }



}
