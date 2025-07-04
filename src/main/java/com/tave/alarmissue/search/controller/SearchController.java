package com.tave.alarmissue.search.controller;

import com.tave.alarmissue.auth.dto.request.PrincipalUserDetails;
import com.tave.alarmissue.news.dto.response.NewsResponseDto;
import com.tave.alarmissue.search.dto.SearchListResponse;
import com.tave.alarmissue.search.dto.SearchResultResponse;
import com.tave.alarmissue.search.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/search/v1")
public class SearchController {

    private final SearchService searchService;

    @GetMapping("/history")
    public ResponseEntity<List<SearchListResponse>> getSearchHistory(@AuthenticationPrincipal PrincipalUserDetails principal) {

        Long userId = principal.getUserId();

        List<SearchListResponse> responseDto = searchService.getSearchHistory(userId);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }


    @GetMapping("")
    public ResponseEntity<SearchResultResponse> searchNews(@AuthenticationPrincipal PrincipalUserDetails principal,
                                                           @RequestParam(value = "title", required = false) String title) {

        Long userId = null;
        if (principal != null) {
            userId = principal.getUserId();
        }

        List<NewsResponseDto> newsList = searchService.searchNews(userId, title);


        List<SearchListResponse> recentSearch = Collections.emptyList();
        if (userId != null) {
            recentSearch = searchService.getSearchHistory(userId);
        }

        SearchResultResponse response = new SearchResultResponse(newsList, recentSearch);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
