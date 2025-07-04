package com.tave.alarmissue.search.converter;

import com.tave.alarmissue.search.domain.Search;
import com.tave.alarmissue.search.dto.SearchListResponse;

import java.util.List;
import java.util.stream.Collectors;

public class SearchConverter {

    public static SearchListResponse toDto(Search search) {
        return SearchListResponse.builder()ㅅ
                .content(search.getContent())
                .build();
    }

    public static List<SearchListResponse> toSearchList(List<Search> searchList) {
        return searchList.stream()
                .map(SearchConverter::toDto)
                .collect(Collectors.toList());
    }
}
