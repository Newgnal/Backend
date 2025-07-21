package com.tave.alarmissue.news.analytics.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tave.alarmissue.news.analytics.domain.DailyNewsAnalytics;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyNewsAnalyticsResponse {
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    private Double avgSentiment;
    private Long newsCount;

    public static DailyNewsAnalyticsResponse from(DailyNewsAnalytics entity) {
        return DailyNewsAnalyticsResponse.builder()
                .date(entity.getAnalyticsDate())
                .avgSentiment(entity.getAvgSentiment())
                .newsCount(entity.getNewsCount())
                .build();
    }
}