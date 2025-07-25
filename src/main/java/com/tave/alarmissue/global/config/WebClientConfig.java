package com.tave.alarmissue.global.config;

import org.springframework.beans.factory.annotation.Value;
import reactor.netty.http.client.HttpClient;
import io.netty.channel.ChannelOption;
import java.time.Duration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;

@Configuration
public class WebClientConfig {

    @Value("${ai.base-url1}")
    private String baseUrl1;

    @Value("${ai.base-url2}")
    private String baseUrl2;

    @Value("${ai.base-url3}")
    private String baseUrl3;


    @Bean
    public WebClient webClientForThema() {
        return WebClient.builder()
                .baseUrl(baseUrl1)
                .build();
    }

    @Bean
    public WebClient webClientForSummary() {
        return WebClient.builder()
                .baseUrl(baseUrl2)
                .build();
    }

    @Bean
    public WebClient webClientForSentiment() {
        HttpClient httpClient = HttpClient.create()
                .responseTimeout(Duration.ofSeconds(100))
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000);

        return WebClient.builder()
                .baseUrl(baseUrl3)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }


}
