package com.tave.alarmissue.search.controller;

import com.tave.alarmissue.auth.dto.request.PrincipalUserDetails;
import com.tave.alarmissue.search.dto.SearchListResponse;
import com.tave.alarmissue.search.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/search/v1")
public class SearchController {

    private final SearchService searchService;

    @GetMapping
    public ResponseEntity<List<SearchListResponse>> getSearchHistory(@AuthenticationPrincipal PrincipalUserDetails principal) {

        Long userId = principal.getUserId();

        List<SearchListResponse> responseDto = searchService.getSearchHistory(userId);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }



}
