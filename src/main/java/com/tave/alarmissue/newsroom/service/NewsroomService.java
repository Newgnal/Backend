package com.tave.alarmissue.newsroom.service;

import com.tave.alarmissue.news.converter.NewsConverter;
import com.tave.alarmissue.newsroom.converter.KeywordConverter;
import com.tave.alarmissue.newsroom.dto.response.KeywordResponse;
import com.tave.alarmissue.newsroom.entity.Keyword;
import com.tave.alarmissue.news.domain.News;
import com.tave.alarmissue.newsroom.dto.response.KeywordNewsResponse;
import com.tave.alarmissue.news.dto.response.NewsDto;
import com.tave.alarmissue.newsroom.repository.KeywordRepository;
import com.tave.alarmissue.news.repository.NewsRepository;
import com.tave.alarmissue.user.domain.UserEntity;
import com.tave.alarmissue.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class NewsroomService {

    private final KeywordRepository keywordRepository;
    private final NewsRepository newsRepository;
    private final UserRepository userRepository;

    // 사용자별 키워드 추가
    public KeywordResponse addKeyword(Long userId, String keywordText) {
        // null 체크 및 trim
        if (keywordText == null || keywordText.trim().isEmpty()) {
            throw new IllegalArgumentException("키워드를 입력해주세요.");
        }

        String trimmedKeyword = keywordText.trim();

        // 길이 검사
        if (trimmedKeyword.length() < 2 || trimmedKeyword.length() > 10) {
            throw new IllegalArgumentException("키워드는 2자 이상 10자 이하로 입력해주세요.");
        }

        if (keywordRepository.existsByUserIdAndKeyword(userId, keywordText)) {
            throw new IllegalArgumentException("이미 존재하는 키워드입니다: " + keywordText);
        }

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        Keyword keyword = Keyword.builder()
                .keyword(trimmedKeyword)
                .user(user)
                .build();

        Keyword savedKeyword = keywordRepository.save(keyword);
        return KeywordConverter.toResponse(savedKeyword);
    }

    // 사용자별 키워드 삭제
    @Transactional
    public void removeKeyword(Long userId, String keywordText) {
        if (!keywordRepository.existsByUserIdAndKeyword(userId, keywordText)) {
            throw new IllegalArgumentException("존재하지 않는 키워드입니다: " + keywordText);
        }
        keywordRepository.deleteByUserIdAndKeyword(userId, keywordText);
    }

    // 사용자의 모든 키워드 조회
    public List<KeywordResponse> getUserKeywords(Long userId) {
        List<Keyword> keywords = keywordRepository.findByUserId(userId);
        return KeywordConverter.toResponseList(keywords);
    }

    // 키워드별 뉴스 조회
    public KeywordNewsResponse getNewsByKeyword(Long userId, String keywordText) {
        // 사용자가 해당 키워드를 등록했는지 확인
        if (!keywordRepository.existsByUserIdAndKeyword(userId, keywordText)) {
            throw new IllegalArgumentException("등록되지 않은 키워드입니다: " + keywordText);
        }

        List<News> newsList = newsRepository.findByTitleContainingIgnoreCaseOrderByDateDesc(keywordText);

        List<NewsDto> newsArticles = NewsConverter.toDtoList(newsList);

        return new KeywordNewsResponse(keywordText, newsArticles);
    }


    // 키워드별 뉴스 개수 조회
    public Map<String, Integer> getUserKeywordNewsCount(Long userId) {
        List<Keyword> keywords = keywordRepository.findByUserIdOrderByCreatedAtAsc(userId);

        return KeywordConverter.toResponseList(keywords).stream()
                .collect(Collectors.toMap(
                        KeywordResponse::getKeyword,
                        k -> newsRepository.countByTitleContainingIgnoreCase(k.getKeyword()),
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }
}
