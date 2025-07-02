package com.tave.alarmissue.newsroom.service;

import com.tave.alarmissue.news.domain.News;
import com.tave.alarmissue.news.dto.response.RepresentativeNewsDto;
import com.tave.alarmissue.news.repository.NewsRepository;
import com.tave.alarmissue.newsroom.dto.response.PopularKeywordResponse;
import com.tave.alarmissue.newsroom.utils.TimeAgoCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PopularKeywordService {

    private final NewsRepository newsRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private static final String RAW_KEY = "popular_keywords_raw";
    private static final String DAILY_KEY = "popular_keywords_daily";


    // 실시간 이벤트 발생 시 호출
    public void increaseKeywordScore(String keyword) {
        redisTemplate.opsForZSet().incrementScore(RAW_KEY, keyword, 1);
    }

    // 하루마다 상위 N개만 별도 Sorted Set에 저장 (배치/스케줄러에서 호출)
    @Transactional
    public void updateDailyPopularKeywords(int topN) {
        // 상위 N개 키워드 조회
        Set<ZSetOperations.TypedTuple<String>> topKeywords =
                redisTemplate.opsForZSet().reverseRangeWithScores(RAW_KEY, 0, topN - 1);

        // 기존 daily set 초기화
        redisTemplate.delete(DAILY_KEY);

        // 상위 N개만 저장
        if (topKeywords != null && !topKeywords.isEmpty()) {
            for (ZSetOperations.TypedTuple<String> tuple : topKeywords) {
                redisTemplate.opsForZSet().add(DAILY_KEY, tuple.getValue(), tuple.getScore());
            }
        }

        redisTemplate.delete(RAW_KEY);
    }

    // 인기 키워드 Top N 조회 (API에서 호출)
    public List<PopularKeywordResponse> getTopKeywords(int topN) {
        Set<ZSetOperations.TypedTuple<String>> result =
                redisTemplate.opsForZSet().reverseRangeWithScores(DAILY_KEY, 0, topN - 1);

        if (result == null) return Collections.emptyList();

        return result.stream()
                .map(tuple ->{
                        String keyword = tuple.getValue();
                Long count = tuple.getScore().longValue();
                RepresentativeNewsDto representativeNews = getRepresentativeNews(keyword);
                return new PopularKeywordResponse(keyword, count, representativeNews);
                })
                .collect(Collectors.toList());
    }

    // 키워드별 대표 뉴스 조회
    private RepresentativeNewsDto getRepresentativeNews(String keyword) {
        Pageable pageable = PageRequest.of(0, 1);
        List<News> newsList = newsRepository.findTopNewsByKeyword(keyword, pageable);

        if (newsList.isEmpty()) {
            return null;
        }
        News representativeNews = newsList.get(0);
        String timeAgo = TimeAgoCalculator.calculateTimeAgo(representativeNews.getDate());

        return RepresentativeNewsDto.builder()
                .title(representativeNews.getTitle())
                .source(representativeNews.getSource())
                .timeAgo(timeAgo)
                .build();
    }
}
