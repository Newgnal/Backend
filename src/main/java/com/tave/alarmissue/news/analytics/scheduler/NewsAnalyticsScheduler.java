package com.tave.alarmissue.news.analytics.scheduler;

import com.tave.alarmissue.news.analytics.domain.DailyNewsAnalytics;
import com.tave.alarmissue.news.analytics.repository.DailyNewsAnalyticsRepository;
import com.tave.alarmissue.news.domain.enums.Thema;
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
        log.info("테마별 집계 시작: {}", targetDate);

        try {
            List<Object[]> results = newsRepository.findDailyStatsByDateGroupByThema(targetDate);

            if (!results.isEmpty()) {
                int processedCount = 0;

                for (Object[] stats : results) {
                    if (stats != null && stats.length >= 3) {
                        Thema thema = (Thema) stats[0];
                        Double avgSentiment = stats[1] != null ?
                                roundToTwoDecimalPlaces(((Number) stats[1]).doubleValue()) : null;
                        Long newsCount = stats[2] != null ? ((Number) stats[2]).longValue() : 0L;

                    if (newsCount > 0) {
                        analyticsRepository.findByAnalyticsDateAndThema(targetDate, thema)
                                .ifPresentOrElse(
                                        existing -> {
                                            existing.updateAnalytics(avgSentiment, newsCount);
                                            analyticsRepository.save(existing);

                                            log.debug("테마별 집계 업데이트: {} - {} (뉴스: {})",
                                                    targetDate, thema, newsCount);
                                        },
                                        () -> {
                                            DailyNewsAnalytics newAnalytics = DailyNewsAnalytics.builder()
                                                    .analyticsDate(targetDate)
                                                    .thema(thema)
                                                    .avgSentiment(avgSentiment)
                                                    .newsCount(newsCount)
                                                    .build();
                                            analyticsRepository.save(newAnalytics);
                                            log.debug("테마별 집계 생성: {} - {} (뉴스: {})",
                                                    targetDate, thema, newsCount);
                                        }
                                );
                        processedCount++;
                    }
                    }
                }

                log.info("테마별 집계 완료: {} ({개 테마 처리)", targetDate, processedCount);

            } else {
                log.warn("집계 데이터 없음: {}", targetDate);
            }
        } catch (Exception e) {
            log.error("테마별 집계 실패: {} - {}", targetDate, e.getMessage());
        }
    }

    /**
     * 소수점 두자리 반올림
     */
    private Double roundToTwoDecimalPlaces(Double value) {
        if (value == null) {
            return null;
        }
        return Math.round(value * 100.0) / 100.0;
    }

}
