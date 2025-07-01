package com.tave.alarmissue.newsroom.service;

import com.tave.alarmissue.global.dto.request.PagenationRequest;
import com.tave.alarmissue.global.dto.response.PagedResponse;
import com.tave.alarmissue.global.exception.CustomException;
import com.tave.alarmissue.global.utils.PagenationUtils;
import com.tave.alarmissue.news.converter.NewsConverter;
import com.tave.alarmissue.news.dto.response.NewsResponseDto;
import com.tave.alarmissue.newsroom.converter.KeywordConverter;
import com.tave.alarmissue.newsroom.dto.response.KeywordResponse;
import com.tave.alarmissue.newsroom.entity.Keyword;
import com.tave.alarmissue.news.domain.News;
import com.tave.alarmissue.newsroom.dto.response.KeywordNewsResponse;
import com.tave.alarmissue.news.dto.response.NewsDto;
import com.tave.alarmissue.newsroom.exception.KeywordErrorCode;
import com.tave.alarmissue.newsroom.repository.KeywordRepository;
import com.tave.alarmissue.news.repository.NewsRepository;
import com.tave.alarmissue.user.domain.UserEntity;
import com.tave.alarmissue.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
public class NewsroomService {

    private final KeywordRepository keywordRepository;
    private final NewsRepository newsRepository;
    private final UserRepository userRepository;
    private final NewsConverter newsConverter;

    // 사용자별 키워드 추가
    @Transactional
    public KeywordResponse addKeyword(Long userId, String keywordText) {
        // null 체크 및 trim
        if (keywordText == null || keywordText.trim().isEmpty()) {
            throw new CustomException(KeywordErrorCode.KEYWORD_EMPTY);
        }

        String trimmedKeyword = keywordText.trim();

        // 길이 검사
        if (trimmedKeyword.length() < 2 || trimmedKeyword.length() > 10) {
            throw new CustomException(KeywordErrorCode.KEYWORD_LENGTH_INVALID);
        }

        if (keywordRepository.existsByUserIdAndKeyword(userId, keywordText)) {
            throw new CustomException(KeywordErrorCode.KEYWORD_ALREADY_EXISTS, trimmedKeyword);
        }

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() ->new CustomException(KeywordErrorCode.USER_NOT_FOUND));

        Keyword keyword = Keyword.builder()
                .keyword(trimmedKeyword)
                .user(user)
                .build();

        Keyword savedKeyword = keywordRepository.save(keyword);
        return KeywordConverter.toResponse(savedKeyword);
    }

    // 사용자별 키워드 삭제
    @Transactional
    public void removeKeyword(Long userId, Long keywordId) {
        Keyword keyword = keywordRepository.findById(keywordId)
                .orElseThrow(() -> new CustomException(KeywordErrorCode.KEYWORD_NOT_FOUND));

        // 사용자 확인
        if (!keyword.getUser().getId().equals(userId)) {
            throw new CustomException(KeywordErrorCode.UNAUTHORIZED_ACCESS);
        }

        keywordRepository.deleteById(keywordId);
    }


    // 키워드별 뉴스 조회
    public KeywordNewsResponse getNewsByKeyword(Long userId, Long keywordId, PagenationRequest pagenationRequest) {
        Keyword keyword = keywordRepository.findById(keywordId)
                .orElseThrow(() -> new CustomException(KeywordErrorCode.KEYWORD_NOT_FOUND, keywordId.toString()));

        // 사용자 확인
        if (!keyword.getUser().getId().equals(userId)) {
            throw new CustomException(KeywordErrorCode.UNAUTHORIZED_ACCESS);
        }

        String keywordText = keyword.getKeyword();

        // 페이지 크기 검증 및 조정
        int pageSize = PagenationUtils.validateAndAdjustPageSize(pagenationRequest.getSize());
        Pageable pageable = PageRequest.of(0, pageSize + 1); // +1로 다음 페이지 존재 여부 확인

        List<News> newsList;

        // 첫 페이지 또는 다음 페이지 처리
        if (pagenationRequest.getLastId() == null) {
            // 첫 페이지 조회
            newsList = newsRepository.findByKeywordOrderByIdDesc(keywordText, pageable);
        } else {
            // 다음 페이지 조회
            newsList = newsRepository.findByKeywordAndIdLessThanOrderByIdDesc(
                    keywordText, pagenationRequest.getLastId(), pageable);
        }

        // 페이지네이션 데이터 처리
        PagenationUtils.PaginationResult<News> paginationResult =
                PagenationUtils.processPaginationData(newsList, pageSize);

        // 다음 요청에 사용할 lastId 설정
        Long nextLastId = null;
        if (paginationResult.isHasNext() && !paginationResult.getData().isEmpty()) {
            nextLastId = paginationResult.getData().get(paginationResult.getData().size() - 1).getId();
        }

        // paginationResult.getData() : 실제 반환할 데이터만 dto로 변환 (newList X)
        List<NewsResponseDto> newsArticles = newsConverter.toDtoList(paginationResult.getData());

        // 전체 개수 조회
        long totalCount = newsRepository.countByKeyword(keywordText);

        // 페이지네이션 응답 생성
        PagedResponse<NewsResponseDto> pagedResponse = PagenationUtils.createPagedResponse(
                newsArticles, paginationResult.isHasNext(), nextLastId, totalCount);


        return new KeywordNewsResponse(keywordText, pagedResponse);
    }


    // 키워드별 뉴스 개수 조회
    public Map<String, Long> getUserKeywordNewsCount(Long userId) {
        List<Keyword> keywords = keywordRepository.findByUserIdOrderByCreatedAtAsc(userId);

        return KeywordConverter.toResponseList(keywords).stream()
                .collect(Collectors.toMap(
                        KeywordResponse::getKeyword,
                        k -> newsRepository.countByKeyword(k.getKeyword()),
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }
}
