package com.tave.alarmissue.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${ai.base-url1}")
    private String baseUrl1;

    @Bean
    public WebClient webClientForThema() {
        return WebClient.builder()
                .baseUrl(baseUrl1)
                .build();
    }


}
