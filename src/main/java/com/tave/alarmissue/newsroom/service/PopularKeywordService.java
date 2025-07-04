package com.tave.alarmissue.newsroom.service;

import com.tave.alarmissue.news.domain.News;
import com.tave.alarmissue.news.dto.response.RepresentativeNewsDto;
import com.tave.alarmissue.news.repository.NewsRepository;
import com.tave.alarmissue.newsroom.dto.response.PopularKeywordResponse;
import com.tave.alarmissue.newsroom.exception.KeywordErrorCode;
import com.tave.alarmissue.newsroom.exception.KeywordException;
import com.tave.alarmissue.newsroom.utils.TimeAgoCalculator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class PopularKeywordService {

    private final NewsRepository newsRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private static final String DAILY_KEY = "popular_keywords_daily";


    // 실시간 키워드 점수 증가
    public void increaseKeywordScore(String keyword) {
        redisTemplate.opsForZSet().incrementScore(DAILY_KEY, keyword, 1);
    }

    // 매일 0시에 초기화만 수행
    public void resetDailyPopularKeywords() {
        log.info("일일 인기 키워드 초기화 시작");

            Long currentCount = redisTemplate.opsForZSet().zCard(DAILY_KEY);
            log.info("초기화 전 DAILY_KEY 키워드 개수: {}", currentCount);

            Boolean deleted = redisTemplate.delete(DAILY_KEY);
            log.info("DAILY_KEY 초기화 결과: {}", deleted);

            if (deleted == null || !deleted) {
                throw new KeywordException(KeywordErrorCode.KEYWORD_RESET_FAILED);
            }

        log.info("DAILY_KEY 초기화 완료");
    }

    // 인기 키워드 Top N 조회 (실시간)
    public List<PopularKeywordResponse> getTopKeywords(int topN) {

        // DAILY_KEY에서 직접 조회
        Set<ZSetOperations.TypedTuple<String>> result =
                redisTemplate.opsForZSet().reverseRangeWithScores(DAILY_KEY, 0, topN - 1);

        if (result == null || result.isEmpty()) {
            return Collections.emptyList();
        }

        return result.stream()
                .map(tuple -> {
                    String keyword = tuple.getValue();
                    Long keywordCount = tuple.getScore().longValue();
                    RepresentativeNewsDto representativeNews = getRepresentativeNews(keyword);
                    return new PopularKeywordResponse(keyword, keywordCount, representativeNews);
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
