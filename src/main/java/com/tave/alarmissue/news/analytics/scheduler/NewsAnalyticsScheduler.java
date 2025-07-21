package com.tave.alarmissue.news.analytics.scheduler;

import com.tave.alarmissue.news.analytics.domain.DailyNewsAnalytics;
import com.tave.alarmissue.news.analytics.repository.DailyNewsAnalyticsRepository;
import com.tave.alarmissue.news.repository.NewsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class NewsAnalyticsScheduler {

    private final NewsRepository newsRepository;
    private final DailyNewsAnalyticsRepository analyticsRepository;

    // 매일 새벽 2시 15분에 전날 데이터 집계
    @Scheduled(cron = "0 15 2 * * *")
    @Transactional
    public void aggregateDailyNews() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        executeAggregation(yesterday);
    }

//    //테스트
//    @Scheduled(cron = "0 */5 * * * *")
//    @Transactional
//    public void testAggregation() {
//        LocalDate today = LocalDate.now();
//        executeAggregation(today);
//    }

    // 집계 로직
    private void executeAggregation(LocalDate targetDate) {
        log.info("일별 집계 시작: {}", targetDate);

        try {
            List<Object[]> results = newsRepository.findDailyStatsByDate(targetDate);

            if (!results.isEmpty()) {
                Object[] stats = results.get(0);

                if (stats != null && stats.length >= 2) {
                    Double avgSentiment = stats[0] != null ? ((Number) stats[0]).doubleValue() : null;
                    Long newsCount = stats[1] != null ? ((Number) stats[1]).longValue() : 0L;

                    if (newsCount > 0) {
                        analyticsRepository.findByAnalyticsDate(targetDate)
                                .ifPresentOrElse(
                                        existing -> {
                                            existing.updateAnalytics(avgSentiment, newsCount);
                                            analyticsRepository.save(existing);
                                            log.info("집계 업데이트: {} (뉴스: {})", targetDate, newsCount);
                                        },
                                        () -> {
                                            DailyNewsAnalytics newAnalytics = DailyNewsAnalytics.builder()
                                                    .analyticsDate(targetDate)
                                                    .avgSentiment(avgSentiment)
                                                    .newsCount(newsCount)
                                                    .build();
                                            analyticsRepository.save(newAnalytics);
                                            log.info("집계 생성: {} (뉴스: {})", targetDate, newsCount);
                                        }
                                );
                    } else {
                        log.warn("뉴스 없음: {}", targetDate);
                    }
                }
            } else {
                log.warn("집계 데이터 없음: {}", targetDate);
            }
        } catch (Exception e) {
            log.error("집계 실패: {} - {}", targetDate, e.getMessage());
        }
    }

}
