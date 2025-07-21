package com.tave.alarmissue.news.analytics.repository;

import com.tave.alarmissue.news.analytics.domain.DailyNewsAnalytics;
import com.tave.alarmissue.news.analytics.dto.response.DailyNewsAnalyticsResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DailyNewsAnalyticsRepository extends JpaRepository<DailyNewsAnalytics, Long> {

    List<DailyNewsAnalytics> findByAnalyticsDateBetweenOrderByAnalyticsDate(
            LocalDate startDate, LocalDate endDate);

    Optional<DailyNewsAnalytics> findByAnalyticsDate(LocalDate date);
}
