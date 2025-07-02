package com.tave.alarmissue;

import com.tave.alarmissue.news.service.DaumNewsCrawlService;
import com.tave.alarmissue.news.service.NaverNewsCrawlService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
@RequiredArgsConstructor
public class AlarmissueApplication implements CommandLineRunner {

	// 추후 스케줄러로 수정할 예정
	private final DaumNewsCrawlService daumNewsCrawlService;
	private final NaverNewsCrawlService naverNewsCrawlService;

	public static void main(String[] args) {
		SpringApplication.run(AlarmissueApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("다음 뉴스 크롤링 시작...");
		daumNewsCrawlService.crawlDaumEconomyNews();

		System.out.println("네이버 뉴스 크롤링 시작...");
		naverNewsCrawlService.crawlNaverEconomyNews();

		System.out.println("뉴스 크롤링 완료.");
	}
}
