package com.tave.alarmissue.news.analytics.controller;

import com.tave.alarmissue.news.analytics.dto.response.SentimentNewsResponse;
import com.tave.alarmissue.news.analytics.scheduler.NewsAnalyticsScheduler;
import com.tave.alarmissue.news.analytics.service.NewsAnalyticsService;
import com.tave.alarmissue.news.domain.enums.Thema;
import com.tave.alarmissue.news.exceptions.NewsErrorCode;
import com.tave.alarmissue.news.exceptions.NewsException;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/news/v1/analytics")
@RequiredArgsConstructor
@Validated
public class NewsAnalyticsController {

    private final NewsAnalyticsService analyticsService;
    private final NewsAnalyticsScheduler scheduler;

    /**
     * 최근 한달 감성지표 및 뉴스 수 집계 조회
     */
    @GetMapping("/thema/{thema}/recent-month")
    @Operation(summary = "한달 감성지표 및 뉴스 수 차트 조회", description = "최근 한달 간의 감성 지표 및 뉴스 수를 집계 조회합니다. (오늘 집계는 실시간 반영)")
    public ResponseEntity<SentimentNewsResponse> getRecentMonthAnalytics(@PathVariable Thema thema)  {
        SentimentNewsResponse response = analyticsService.getRecentMonthAnalytics(thema);
        return ResponseEntity.ok(response);
    }

    /**
     * 기간별 감성지표 및 뉴스 수 집계 조회
     */
    @GetMapping("/thema/{thema}/period")
    @Operation(summary = "기간별 감성지표 및 뉴스 수 차트 조회", description = "기간 별로 감성 지표 및 뉴스 수를 집계 조회합니다.")
    public ResponseEntity<SentimentNewsResponse> getAnalyticsByPeriod(
            @PathVariable Thema thema,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd")
            @NotNull(message = "시작날짜는 필수입니다") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd")
            @NotNull(message = "종료날짜는 필수입니다") LocalDate endDate) {

        SentimentNewsResponse response = analyticsService.getAnalyticsBetween(thema,startDate, endDate);
        return ResponseEntity.ok(response);
    }

}
