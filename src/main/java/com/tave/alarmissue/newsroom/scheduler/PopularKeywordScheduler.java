package com.tave.alarmissue.newsroom.scheduler;

import com.tave.alarmissue.newsroom.service.PopularKeywordService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PopularKeywordScheduler {

    private final PopularKeywordService popularKeywordService;
    private static final int DEFAULT_TOP_COUNT = 10;

    // 매일 0시에 초기화만 수행
    @Scheduled(cron = "0 0 0 * * *")
    public void resetDailyPopularKeywords() {
        popularKeywordService.resetDailyPopularKeywords();
    }
}
