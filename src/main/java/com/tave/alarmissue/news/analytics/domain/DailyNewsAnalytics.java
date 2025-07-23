package com.tave.alarmissue.news.analytics.domain;

import com.tave.alarmissue.global.domain.BaseTimeEntity;
import com.tave.alarmissue.news.domain.enums.Thema;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "daily_news_analytics",
        uniqueConstraints = @UniqueConstraint(columnNames = {"analyticsDate", "thema"}))
@NoArgsConstructor(access = PROTECTED)
@Getter
public class DailyNewsAnalytics extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate analyticsDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Thema thema;

    @Column(nullable = false)
    private Double avgSentiment;

    @Column(nullable = false)
    private Long newsCount;

    @Builder
    public DailyNewsAnalytics(LocalDate analyticsDate,Thema thema, Double avgSentiment, Long newsCount) {
        this.analyticsDate = analyticsDate;
        this.thema = thema;
        this.avgSentiment = avgSentiment;
        this.newsCount = newsCount;
    }

    public void updateAnalytics(Double avgSentiment, Long newsCount) {
        this.avgSentiment = avgSentiment;
        this.newsCount = newsCount;
    }
}
