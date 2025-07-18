package com.tave.alarmissue.search.service;

import com.tave.alarmissue.news.converter.NewsConverter;
import com.tave.alarmissue.news.domain.News;
import com.tave.alarmissue.news.domain.enums.Thema;
import com.tave.alarmissue.news.dto.response.NewsResponseDto;
import com.tave.alarmissue.news.repository.NewsRepository;
import com.tave.alarmissue.search.converter.SearchConverter;
import com.tave.alarmissue.search.domain.Search;
import com.tave.alarmissue.search.dto.SearchListResponse;
import com.tave.alarmissue.search.repository.SearchRepository;
import com.tave.alarmissue.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SearchService {

    private final NewsRepository newsRepository;
    private final SearchRepository searchRepository;
    private final NewsConverter newsConverter;
    private final UserRepository userRepository;

    public List<SearchListResponse> getSearchHistory(Long userId) {

        List<Search> searchList = searchRepository.findByUserIdOrderByIdDesc(userId);

        return SearchConverter.toSearchList(searchList);

    }

    @Transactional
    public Slice<NewsResponseDto> searchNewsSlice(Long userId, String title, int page, int size) {

        if (title == null || title.isBlank()) {
            return emptySlice();
        }

        saveSearchHistory(userId, title);

        Pageable pageable = PageRequest.of(page, size);

        Slice<News> newsSlice = newsRepository.findByTitleContainingIgnoreCaseOrderByDateDesc(title, pageable);

        // Slice<News> → Slice<NewsResponseDto> 변환
        return newsSlice.map(newsConverter::toDto);
    }

    /*
    private method 분리
     */

    // null 처리
    private Slice<NewsResponseDto> emptySlice() {
        Pageable pageable = PageRequest.of(0, 1);
        return new SliceImpl<>(Collections.emptyList(), pageable, false);
    }

    // 검색 기록 저장
    private void saveSearchHistory(Long userId, String title) {
        if (userId != null && !searchRepository.existsByUserIdAndContent(userId, title)) {
            Search search = Search.create(title, userRepository.getReferenceById(userId));
            searchRepository.save(search);
        }
    }
}