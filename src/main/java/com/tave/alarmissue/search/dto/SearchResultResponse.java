package com.tave.alarmissue.search.dto;

import com.tave.alarmissue.news.dto.response.NewsResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class SearchResultResponse {
    private List<NewsResponseDto> newsList;
    private List<SearchListResponse> recentSearches;
    private boolean hasNext;
}