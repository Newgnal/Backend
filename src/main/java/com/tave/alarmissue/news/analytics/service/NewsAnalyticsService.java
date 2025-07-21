package com.tave.alarmissue.news.analytics.service;


import com.tave.alarmissue.news.analytics.domain.DailyNewsAnalytics;
import com.tave.alarmissue.news.analytics.dto.response.AnalyticsSummaryResponse;
import com.tave.alarmissue.news.analytics.dto.response.DailyNewsAnalyticsResponse;
import com.tave.alarmissue.news.analytics.dto.response.SentimentNewsResponse;
import com.tave.alarmissue.news.analytics.repository.DailyNewsAnalyticsRepository;
import com.tave.alarmissue.news.exceptions.NewsErrorCode;
import com.tave.alarmissue.news.exceptions.NewsException;
import com.tave.alarmissue.news.repository.NewsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class NewsAnalyticsService {

    private final DailyNewsAnalyticsRepository analyticsRepository;
    private final NewsRepository newsRepository;

    public SentimentNewsResponse getRecentMonthAnalytics() {
        LocalDate today = LocalDate.now();
        LocalDate startDate = today.minusDays(29); // 30일간 (오늘 포함)

        return getAnalyticsBetween(startDate, today);
    }

    public SentimentNewsResponse getAnalyticsBetween(LocalDate startDate, LocalDate endDate) {
        // 날짜 유효성 검사
        if (startDate.isAfter(endDate)) {
            throw new NewsException(NewsErrorCode.INVALID_DATE_RANGE);
        }

        if (ChronoUnit.DAYS.between(startDate, endDate) > 365) {
            throw new NewsException(NewsErrorCode.DATE_RANGE_TOO_LONG);
        }

        LocalDate today = LocalDate.now();
        List<DailyNewsAnalyticsResponse> responseDtoList = new ArrayList<>();

        // 1. 과거 데이터는 집계 테이블에서 조회
        if (startDate.isBefore(today)) {
            LocalDate pastEndDate = endDate.isBefore(today) ? endDate : today.minusDays(1);
            List<DailyNewsAnalytics> pastData = analyticsRepository
                    .findByAnalyticsDateBetweenOrderByAnalyticsDate(startDate, pastEndDate);

            List<DailyNewsAnalyticsResponse> pastResponses = pastData.stream()
                    .map(DailyNewsAnalyticsResponse::from)
                    .collect(Collectors.toList());

            responseDtoList.addAll(pastResponses);
        }

        // 2. 오늘 데이터는 실시간 계산
        if (!endDate.isBefore(today)) {
            DailyNewsAnalyticsResponse todayData = getTodayAnalyticsRealtime(today);
            if (todayData != null) {
                responseDtoList.add(todayData);
            }
        }

        if (responseDtoList.isEmpty()) {
            throw new NewsException(NewsErrorCode.ANALYTICS_DATA_NOT_FOUND);
        }

        // 날짜순 정렬
        responseDtoList.sort(Comparator.comparing(DailyNewsAnalyticsResponse::getDate));

        AnalyticsSummaryResponse summary = calculateSummaryFromDto(responseDtoList, startDate, endDate);

        return SentimentNewsResponse.builder()
                .dailyData(responseDtoList)
                .summary(summary)
                .build();
    }

    //실시간 계산
    private DailyNewsAnalyticsResponse getTodayAnalyticsRealtime(LocalDate today) {
        try {
            List<Object[]> results = newsRepository.findDailyStatsByDate(today);

            if (!results.isEmpty()) {
                Object[] stats = results.get(0);

                if (stats != null && stats.length >= 2) {
                    Double avgSentiment = stats[0] != null ? ((Number) stats[0]).doubleValue() : null;
                    Long newsCount = stats[1] != null ? ((Number) stats[1]).longValue() : 0L;

                    if (newsCount > 0) {
                        return DailyNewsAnalyticsResponse.builder()
                                .date(today)
                                .avgSentiment(avgSentiment)
                                .newsCount(newsCount)
                                .build();
                    }
                }
            }
        } catch (Exception e) {
            log.warn("오늘 데이터 실시간 계산 실패: {}", today, e);
        }

        return null;
    }

    //결과 요약
    private AnalyticsSummaryResponse calculateSummaryFromDto(List<DailyNewsAnalyticsResponse> dailyData,
                                                             LocalDate startDate, LocalDate endDate) {

        long totalNewsCount = dailyData.stream()
                .mapToLong(DailyNewsAnalyticsResponse::getNewsCount)
                .sum();

        double overallAvgSentiment = 0.0;
        if (totalNewsCount > 0) {
            overallAvgSentiment = dailyData.stream()
                    .filter(d -> d.getAvgSentiment() != null)
                    .mapToDouble(d -> d.getAvgSentiment() * d.getNewsCount())
                    .sum() / totalNewsCount;
        }

        return AnalyticsSummaryResponse.builder()
                .overallAvgSentiment(overallAvgSentiment)
                .totalNewsCount(totalNewsCount)
                .startDate(startDate)
                .endDate(endDate)
                .build();
    }
}
