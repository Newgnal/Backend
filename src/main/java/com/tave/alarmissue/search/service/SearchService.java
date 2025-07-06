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
    public List<NewsResponseDto> searchNews(Long userId, String title) {

        if (title == null || title.isBlank()) {
            return Collections.emptyList();
        }

        // 로그인한 사용자
        if (userId!=null) {
            boolean alreadyExists = searchRepository.existsByUserIdAndContent(userId, title);
            if (!alreadyExists) {
                Search search = Search.create(title, userRepository.getReferenceById(userId));
                searchRepository.save(search);
            }
        }

        // themaEnum 과 title -> newsRepository
        List<News> newsList = newsRepository.findByTitleContainingIgnoreCase(title);

        return newsConverter.toDtoList(newsList);
    }
}