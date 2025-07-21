package com.tave.alarmissue.news.analytics.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnalyticsSummaryResponse {
    private Double overallAvgSentiment;
    private Long totalNewsCount;
    private LocalDate startDate;
    private LocalDate endDate;
}
