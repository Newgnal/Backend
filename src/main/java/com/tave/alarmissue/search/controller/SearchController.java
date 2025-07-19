package com.tave.alarmissue.search.controller;

import com.tave.alarmissue.auth.dto.request.PrincipalUserDetails;
import com.tave.alarmissue.news.dto.response.NewsResponseDto;
import com.tave.alarmissue.search.dto.SearchListResponse;
import com.tave.alarmissue.search.dto.SearchResultResponse;
import com.tave.alarmissue.search.service.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
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

    @Operation(summary = "뉴스 검색")
    @GetMapping("")
    public ResponseEntity<SearchResultResponse> searchNews(@AuthenticationPrincipal PrincipalUserDetails principal,
                                                           @RequestParam(value = "title", required = false) String title,
                                                           @RequestParam(value = "page", defaultValue = "0") int page,
                                                           @RequestParam(value = "size", defaultValue = "20") int size) {

        Long userId = null;
        if (principal != null) {
            userId = principal.getUserId();
        }

        Slice<NewsResponseDto> searchSlice = searchService.searchNewsSlice(userId, title, page, size);

        SearchResultResponse response = SearchResultResponse.builder()
                .newsList(searchSlice.getContent())
                .recentSearches(getRecentSearches(userId))
                .hasNext(searchSlice.hasNext())
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    private List<SearchListResponse> getRecentSearches(Long userId) {
        if (userId == null)
            return Collections.emptyList();
        return searchService.getSearchHistory(userId);
    }

}
