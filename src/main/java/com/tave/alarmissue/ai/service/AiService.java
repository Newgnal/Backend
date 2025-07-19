package com.tave.alarmissue.ai.service;

import com.tave.alarmissue.ai.dto.response.SentimentResponse;
import com.tave.alarmissue.ai.dto.response.SummaryResponse;
import com.tave.alarmissue.ai.dto.response.ThemaResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AiService {

    private final WebClient webClientForThema;
    private final WebClient webClient;

    public Mono<ThemaResponse> analyzeThema(String text) {
        return webClientForThema.post()
                .uri("/predict")
                .contentType(MediaType.APPLICATION_JSON)  // 요청 바디 타입
                .bodyValue(Map.of("text", text))
                .retrieve()
                .bodyToMono(ThemaResponse.class);
    }

    public Mono<SummaryResponse> analyzeSummary(String text) {
        return webClient.post()
                .uri("/summarize")
                .bodyValue(Map.of("text", text))
                .retrieve()
                .bodyToMono(SummaryResponse.class);
    }


        public Mono<SentimentResponse> analyzeSentiment(List<String> titles) {
        return webClient.post()
                .uri("/sentiment")
                .bodyValue(Map.of("titles", titles))
                .retrieve()
                .bodyToMono(SentimentResponse.class);
    }


}
