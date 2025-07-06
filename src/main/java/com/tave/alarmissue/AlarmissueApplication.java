package com.tave.alarmissue;

import com.tave.alarmissue.news.service.DaumNewsCrawlService;
import com.tave.alarmissue.news.service.NaverNewsCrawlService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
@RequiredArgsConstructor
@EnableScheduling
@EnableAsync
public class AlarmissueApplication {

	public static void main(String[] args) {
		SpringApplication.run(AlarmissueApplication.class, args);
	}


}
