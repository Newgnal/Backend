package com.tave.alarmissue.news.analytics.controller;

import com.tave.alarmissue.news.analytics.domain.DailyNewsAnalytics;
import com.tave.alarmissue.news.analytics.repository.DailyNewsAnalyticsRepository;
import com.tave.alarmissue.news.domain.enums.Thema;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/api/v1/test/dummy")
@RequiredArgsConstructor
public class DummyDataController {

    private final DailyNewsAnalyticsRepository analyticsRepository;
    private final Random random = new Random();

    /**
     * 한달간 더미 데이터 생성
     */
    @PostMapping("/create-month-data")
    public ResponseEntity<String> createMonthDummyData(
            @RequestParam(defaultValue = "30") int days) {

        LocalDate endDate = LocalDate.now().minusDays(1); // 어제까지
        LocalDate startDate = endDate.minusDays(days - 1);

        int totalCreated = 0;

        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            List<DailyNewsAnalytics> dayData = createDayData(date);
            analyticsRepository.saveAll(dayData);
            totalCreated += dayData.size();
        }

        return ResponseEntity.ok(
                String.format("더미 데이터 생성 완료: %d일간, 총 %d건 (%s ~ %s)",
                        days, totalCreated, startDate, endDate));
    }

    /**
     * 특정 날짜 더미 데이터 생성
     */
    @PostMapping("/create-day-data")
    public ResponseEntity<String> createDayDummyData(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {

        List<DailyNewsAnalytics> dayData = createDayData(date);
        analyticsRepository.saveAll(dayData);

        return ResponseEntity.ok(
                String.format("더미 데이터 생성 완료: %s (%d건)", date, dayData.size()));
    }

    /**
     * 하루치 데이터 생성 로직
     */
    private List<DailyNewsAnalytics> createDayData(LocalDate date) {
        List<DailyNewsAnalytics> dayData = new ArrayList<>();

        // 총 뉴스 수: 500~600개
        int totalNewsCount = 500 + random.nextInt(101); // 500~600

        // 테마별 분배 비율 (총 100%)
        Map<Thema, Double> themaRatio = getThemaDistribution();

        for (Map.Entry<Thema, Double> entry : themaRatio.entrySet()) {
            Thema thema = entry.getKey();
            double ratio = entry.getValue();

            // 테마별 뉴스 수 계산 (비율 기반)
            long newsCount = Math.round(totalNewsCount * ratio);

            if (newsCount > 0) {
                // 테마별 감성점수 생성 (테마 특성 반영)
                Double avgSentiment = generateSentimentByThema(thema, date);

                DailyNewsAnalytics analytics = DailyNewsAnalytics.builder()
                        .analyticsDate(date)
                        .thema(thema)
                        .avgSentiment(roundToTwoDecimalPlaces(avgSentiment))
                        .newsCount(newsCount)
                        .build();

                dayData.add(analytics);
            }
        }

        return dayData;
    }

    /**
     * 금융/투자 테마별 분배 비율 설정
     */
    private Map<Thema, Double> getThemaDistribution() {
        Map<Thema, Double> distribution = new HashMap<>();

        // 금융 시장 뉴스 분포를 반영한 비율
        distribution.put(Thema.SEMICONDUCTOR_AI, 0.15);          // 15% - 반도체·AI (핫한 섹터)
        distribution.put(Thema.IT_INTERNET, 0.12);               // 12% - IT·인터넷 서비스
        distribution.put(Thema.FINANCE_INSURANCE, 0.14);         // 14% - 금융·보험 (기본 금융)
        distribution.put(Thema.MOBILITY, 0.10);                  // 10% - 자동차·모빌리티
        distribution.put(Thema.DEFENSE_AEROSPACE, 0.07);         // 7% - 방산·항공우주
        distribution.put(Thema.SECOND_BATTERY_ENVIRONMENT, 0.09); // 9% - 2차전지·친환경 에너지
        distribution.put(Thema.REAL_ESTATE_REIT, 0.11);          // 11% - 부동산·리츠
        distribution.put(Thema.BOND_INTEREST, 0.08);             // 8% - 채권·금리
        distribution.put(Thema.HEALTHCARE_BIO, 0.06);            // 6% - 헬스케어·바이오
        distribution.put(Thema.EXCHANGE_RATE, 0.04);             // 4% - 환율·외환
        distribution.put(Thema.RAW_MATERIAL_METALS, 0.03);       // 3% - 원자재·귀금속
        distribution.put(Thema.ETC, 0.01);                       // 1% - 기타

        return distribution;
    }

    /**
     * 테마별 특성을 반영한 감성점수 생성
     */
    private Double generateSentimentByThema(Thema thema, LocalDate date) {
        double baseSentiment = 0.0;
        double variance = 0.3; // 기본 변동폭

        // 테마별 기본 감성 경향 (투자 관점 반영)
        switch (thema) {
            case SEMICONDUCTOR_AI:
                baseSentiment = 0.20; // AI 붐으로 매우 긍정적
                variance = 0.35;
                break;
            case IT_INTERNET:
                baseSentiment = 0.15; // IT 섹터 긍정적
                variance = 0.30;
                break;
            case FINANCE_INSURANCE:
                baseSentiment = 0.05; // 금융은 안정적
                variance = 0.25;
                break;
            case MOBILITY:
                baseSentiment = 0.10; // 전기차 등 긍정적
                variance = 0.40; // 변동성 큼
                break;
            case DEFENSE_AEROSPACE:
                baseSentiment = 0.12; // 방산 관련 긍정적
                variance = 0.35;
                break;
            case SECOND_BATTERY_ENVIRONMENT:
                baseSentiment = 0.18; // 친환경 에너지 긍정적
                variance = 0.30;
                break;
            case REAL_ESTATE_REIT:
                baseSentiment = -0.05; // 부동산 시장 불안
                variance = 0.25;
                break;
            case BOND_INTEREST:
                baseSentiment = -0.08; // 금리 인상 부담
                variance = 0.20;
                break;
            case HEALTHCARE_BIO:
                baseSentiment = 0.08; // 바이오 중립~긍정
                variance = 0.35;
                break;
            case EXCHANGE_RATE:
                baseSentiment = -0.03; // 환율 불안
                variance = 0.30;
                break;
            case RAW_MATERIAL_METALS:
                baseSentiment = 0.02; // 원자재 중립
                variance = 0.40; // 변동성 큼
                break;
            case ETC:
                baseSentiment = 0.00; // 기타는 중립
                variance = 0.25;
                break;
        }

        // 날짜별 변동성 추가 (주말효과, 월말효과 등)
        baseSentiment += getDateVariation(date);

        // 랜덤 변동 추가
        double randomVariation = (random.nextGaussian() * variance);
        double finalSentiment = baseSentiment + randomVariation;

        // -1.0 ~ 1.0 범위로 제한
        return Math.max(-1.0, Math.min(1.0, finalSentiment));
    }

    /**
     * 날짜별 변동성 (주말, 월말 등)
     */
    private double getDateVariation(LocalDate date) {
        double variation = 0.0;

        // 주말 효과 (토요일, 일요일은 뉴스 적음, 약간 부정적)
        if (date.getDayOfWeek().getValue() >= 6) {
            variation -= 0.03;
        }

        // 월말/월초 효과 (실적 발표 시기)
        int dayOfMonth = date.getDayOfMonth();
        if (dayOfMonth <= 3 || dayOfMonth >= 28) {
            variation += 0.05; // 실적 시즌으로 관심 증가
        }

        // 월중 효과
        if (dayOfMonth >= 14 && dayOfMonth <= 16) {
            variation -= 0.02; // 월중에는 약간 조정
        }

        // 분기말 효과 (3, 6, 9, 12월 말)
        if ((date.getMonthValue() % 3 == 0) && dayOfMonth >= 25) {
            variation += 0.08; // 분기말 실적 발표로 관심 증가
        }

        return variation;
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

    /**
     * 기존 더미 데이터 삭제
     */
    @DeleteMapping("/clear-dummy-data")
    public ResponseEntity<String> clearDummyData() {
        long deletedCount = analyticsRepository.count();
        analyticsRepository.deleteAll();
        return ResponseEntity.ok("더미 데이터 " + deletedCount + "건 삭제 완료");
    }
}
