package com.tave.alarmissue.news.service;

import com.tave.alarmissue.news.repository.NewsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class NewsDeleteService {

    private final NewsRepository newsRepository;

    // 매주 일요일 자정에 실행
    @Scheduled(cron = "0 0 0 * * SUN")
    public void deleteOldNews() {
        LocalDateTime oneWeekAgo = LocalDateTime.now().minusWeeks(1);
        int deletedCount = newsRepository.deleteByDateBefore(oneWeekAgo);
        log.info("[NewsDelete] 1주일 지난 뉴스 {}건 삭제 완료", deletedCount);
    }

//    // 5분마다 실행 (테스트용)
//    @Scheduled(cron = "0 */5 * * * *")
//    public void deleteOldNews() {
//        LocalDateTime oneWeekAgo = LocalDateTime.now().minusWeeks(1);
//        int deletedCount = newsRepository.deleteByDateBefore(oneWeekAgo);
//        log.info("[NewsDelete] 5분 지난 뉴스 {}건 삭제 완료", deletedCount);
//    }
}
