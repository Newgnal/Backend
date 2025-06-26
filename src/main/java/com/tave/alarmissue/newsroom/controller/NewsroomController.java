package com.tave.alarmissue.newsroom.controller;

import com.tave.alarmissue.newsroom.dto.request.KeywordRequest;
import com.tave.alarmissue.newsroom.dto.response.KeywordResponse;
import com.tave.alarmissue.newsroom.entity.Keyword;
import com.tave.alarmissue.newsroom.dto.response.KeywordNewsResponse;
import com.tave.alarmissue.newsroom.service.NewsroomService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/newsroom")
@Slf4j
@RequiredArgsConstructor
public class NewsroomController {

    private final NewsroomService newsroomService;

    // 키워드 등록
    @PostMapping("/keywords/{userId}")
    public ResponseEntity<KeywordResponse> addKeyword(@PathVariable Long userId,
                                                      @Valid @RequestBody KeywordRequest request) {
        KeywordResponse response = newsroomService.addKeyword(userId, request.getKeyword());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    // 사용자별 키워드 삭제
    @DeleteMapping("keywords/{keyword}")
    public ResponseEntity<String> removeKeyword(@Parameter Long userId,
                                                @PathVariable String keyword) {
        newsroomService.removeKeyword(userId, keyword);
        return ResponseEntity.status(HttpStatus.OK).body("키워드가 삭제되었습니다.");
    }

    // 사용자의 키워드별 뉴스 개수 조회
    @GetMapping("/keywords/count")
    public ResponseEntity<Map<String, Integer>> getUserKeywordNewsCount(@Parameter Long userId) {
        Map<String, Integer> response = newsroomService.getUserKeywordNewsCount(userId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 특정 키워드의 뉴스 조회
    @GetMapping("/keywords/{keyword}/news")
    public ResponseEntity<KeywordNewsResponse> getNewsByKeyword(@Parameter Long userId,
                                                                @PathVariable String keyword) {
        KeywordNewsResponse response = newsroomService.getNewsByKeyword(userId, keyword);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
