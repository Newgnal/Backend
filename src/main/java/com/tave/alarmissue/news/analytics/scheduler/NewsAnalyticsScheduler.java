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

//     매일 새벽 2시에 전날 데이터 집계
@Scheduled(cron = "0 15 2 * * *")
@Transactional
public void aggregateDailyNews() {
    LocalDate yesterday = LocalDate.now().minusDays(1);

    log.info("일별 뉴스 분석 집계 시작: {}", yesterday);

    try {
        List<Object[]> results = newsRepository.findDailyStatsByDate(yesterday);

        if (!results.isEmpty()) {
            Object[] stats = results.get(0);

            if (stats != null && stats.length >= 2) {
                Double avgSentiment = stats[0] != null ? ((Number) stats[0]).doubleValue() : null;
                Long newsCount = stats[1] != null ? ((Number) stats[1]).longValue() : 0L;

                log.info("집계 결과 - 뉴스 수: {}, 평균 감성: {}", newsCount, avgSentiment);

                if (newsCount > 0) {
                    analyticsRepository.findByAnalyticsDate(yesterday)
                            .ifPresentOrElse(
                                    existing -> {
                                        existing.updateAnalytics(avgSentiment, newsCount);
                                        analyticsRepository.save(existing);
                                        log.info("기존 데이터 업데이트 완료: {}", yesterday);
                                    },
                                    () -> {
                                        DailyNewsAnalytics newAnalytics = DailyNewsAnalytics.builder()
                                                .analyticsDate(yesterday)
                                                .avgSentiment(avgSentiment)
                                                .newsCount(newsCount)
                                                .build();
                                        analyticsRepository.save(newAnalytics);
                                        log.info("신규 데이터 생성 완료: {}", yesterday);
                                    }
                            );
                }
            }
        }
    } catch (Exception e) {
        log.error("일별 뉴스 분석 집계 실패: {}", yesterday, e);
    }
}

}
