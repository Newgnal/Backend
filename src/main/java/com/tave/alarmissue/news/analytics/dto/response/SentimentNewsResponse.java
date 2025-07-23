package com.tave.alarmissue.news.analytics.dto.response;

import com.tave.alarmissue.news.domain.enums.Thema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SentimentNewsResponse {
    private Thema thema;
    private List<DailyNewsAnalyticsResponse> dailyData;
    private AnalyticsSummaryResponse summary;
}
