package com.tave.alarmissue.news.controller;

import com.tave.alarmissue.news.domain.News;
import com.tave.alarmissue.news.domain.enums.Thema;
import com.tave.alarmissue.news.dto.request.NewsSortType;
import com.tave.alarmissue.news.dto.response.NewsResponseDto;
import com.tave.alarmissue.news.dto.response.SliceResponseDto;
import com.tave.alarmissue.news.service.NewsService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.websocket.OnOpen;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class NewsController {

    private final NewsService newsService;

//    @GetMapping("/news")
//    public ResponseEntity<List<NewsResponseDto>> getAllNewsLatest( Pageable pageable) {
//
//        List<NewsResponseDto> response = newsService.getAllNewsLatest(pageable);
//        return ResponseEntity.ok(response);
//    }
//
//
//    //전체 뉴스 조회(조회수순)
//    @GetMapping("/news/viewest")
//    public ResponseEntity<List<NewsResponseDto>> getAllNewsViewest(Pageable pageable){
//        List<NewsResponseDto> response=newsService.getAllNewsViewest(pageable);
//        return ResponseEntity.ok(response);
//    }

    //전체 뉴스 조회(최신순)
//    @GetMapping("/news")
//    public ResponseEntity<SliceResponseDto<NewsResponseDto>> getAllNewsLatest(@RequestParam(defaultValue = "0")int page, @RequestParam(defaultValue = "5") int size) {
//        Pageable pageable= PageRequest.of(page,size);
//        SliceResponseDto<NewsResponseDto> response = newsService.getAllNewsLatest(pageable);
//        return ResponseEntity.ok(response);
//    }
//
//    //전체 뉴스 조회(조회수순)
//    @GetMapping("/news/viewest")
//    public ResponseEntity<SliceResponseDto<NewsResponseDto>> getAllNewsViewest(@RequestParam(defaultValue = "0")int page, @RequestParam(defaultValue = "5") int size){
//        Pageable pageable= PageRequest.of(page,size);
//        SliceResponseDto<NewsResponseDto> response=newsService.getAllNewsViewest(pageable);
//        return ResponseEntity.ok(response);
//    }

    @Operation(summary = "뉴스 전체 조회")
    @GetMapping("/news")
    public ResponseEntity<SliceResponseDto<NewsResponseDto>> getAllNews(@RequestParam(defaultValue = "LATEST")NewsSortType sortType, @RequestParam(defaultValue = "0")int page, @RequestParam(defaultValue = "5") int size) {
        Pageable pageable= PageRequest.of(page,size,sortType.getSort());
        SliceResponseDto<NewsResponseDto> response = newsService.getAllNews(sortType,pageable);
        return ResponseEntity.ok(response);
    }
    @Operation(summary = "테마별 뉴스 전체 조회")
    @GetMapping("/news/thema/{thema}")
    public ResponseEntity<SliceResponseDto<NewsResponseDto>> getAllThemaNews(@RequestParam(defaultValue = "LATEST")NewsSortType sortType,@RequestParam(defaultValue = "0")int page, @RequestParam(defaultValue = "5") int size,@PathVariable Thema thema) {
        Pageable pageable= PageRequest.of(page,size,sortType.getSort());
        SliceResponseDto<NewsResponseDto> response = newsService.getAllThemaNews(sortType,thema,pageable);
        return ResponseEntity.ok(response);
    }


    //테마별 전체 뉴스 조회(최신순)
//    @GetMapping("/news/thema/{thema}")
//    public ResponseEntity<SliceResponseDto<NewsResponseDto>> getThemaNews(@RequestParam(defaultValue = "0")int page, @RequestParam(defaultValue = "5") int size,@PathVariable Thema thema) {
//        Pageable pageable= PageRequest.of(page,size);
//        SliceResponseDto<NewsResponseDto> response = newsService.getThemaNewsLatest(thema,pageable);
//        return ResponseEntity.ok(response);
//    }
//
//    //테마별 전체 뉴스 조회(조회수순)
//    @GetMapping("/news/thema/viewest/{thema}")
//    public ResponseEntity<SliceResponseDto<NewsResponseDto>> getThemaNewsViewest(@RequestParam(defaultValue = "0")int page, @RequestParam(defaultValue = "5") int size,@PathVariable Thema thema) {
//        Pageable pageable= PageRequest.of(page,size);
//        SliceResponseDto<NewsResponseDto> response = newsService.getThemaNewsViewst(thema,pageable);
//        return ResponseEntity.ok(response);
//    }

//    //전체 뉴스 조회(최신순)
//    @GetMapping("/news")
//    public ResponseEntity<List<NewsResponseDto>> getAllNewsLatest() {
//
//        List<NewsResponseDto> response = newsService.getAllNewsLatest();
//        return ResponseEntity.ok(response);
//    }
//
//    //전체 뉴스 조회(조회수순)
//    @GetMapping("/news/viewest")
//    public ResponseEntity<List<NewsResponseDto>> getAllNewsViewest(){
//        List<NewsResponseDto> response=newsService.getAllNewsViewest();
//        return ResponseEntity.ok(response);
//    }

    //테마별 전체 뉴스 조회(최신순)
//    @GetMapping("/news/thema/{thema}")
//    public ResponseEntity<List<NewsResponseDto>> getThemaNews(@PathVariable Thema thema) {
//        List<NewsResponseDto> response = newsService.getThemaNewsLatest(thema);
//        return ResponseEntity.ok(response);
//    }
//
//    //테마별 전체 뉴스 조회(조회수순)
//    @GetMapping("/news/thema/viewest/{thema}")
//    public ResponseEntity<List<NewsResponseDto>> getThemaNewsViewest(@PathVariable Thema thema) {
//        List<NewsResponseDto> response = newsService.getThemaNewsViewst(thema);
//        return ResponseEntity.ok(response);
//    }




}
