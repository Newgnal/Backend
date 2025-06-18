package com.tave.alarmissue;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class AlarmissueApplication {

	public static void main(String[] args) {
		SpringApplication.run(AlarmissueApplication.class, args);
	}

}
