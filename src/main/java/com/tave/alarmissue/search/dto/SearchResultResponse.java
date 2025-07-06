package com.tave.alarmissue.search.dto;

import com.tave.alarmissue.news.dto.response.NewsResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class SearchResultResponse {
    private List<NewsResponseDto> newsList;
    private List<SearchListResponse> recentSearches;
}