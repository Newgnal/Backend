package com.tave.alarmissue.ai.service;

import com.tave.alarmissue.ai.dto.response.SummaryResponse;
import com.tave.alarmissue.ai.dto.response.ThemaResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AiService {

    private final WebClient webClient;

    public Mono<ThemaResponse> analyzeThema(String contentUrl) {
        return webClient.post()
                .uri("/predict")
                .bodyValue(Map.of("contentUrl", contentUrl))
                .retrieve()
                .bodyToMono(ThemaResponse.class);
    }

    public Mono<SummaryResponse> analyzeSummary(String contentUrl) {
        return webClient.post()
                .uri("/summarize")
                .bodyValue(Map.of("contentUrl", contentUrl))
                .retrieve()
                .bodyToMono(SummaryResponse.class);
    }


}
