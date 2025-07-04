package com.tave.alarmissue.search.service;

import com.tave.alarmissue.search.converter.SearchConverter;
import com.tave.alarmissue.search.domain.Search;
import com.tave.alarmissue.search.dto.SearchListResponse;
import com.tave.alarmissue.search.repository.SearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SearchService {

    private final SearchRepository searchRepository;

    public List<SearchListResponse> getSearchHistory(Long userId) {

        List<Search> searchList = searchRepository.findByUserIdOrderByIdDesc(userId);

        return SearchConverter.toSearchList(searchList);

    }

}
