package com.tave.alarmissue.ai.service;

import com.tave.alarmissue.ai.dto.response.SentimentResponse;
import com.tave.alarmissue.ai.dto.response.SummaryResponse;
import com.tave.alarmissue.ai.dto.response.ThemaResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AiService {

    private final WebClient webClientForThema;
    private final WebClient webClientForSummary;
    private final WebClient webClientForSentiment;

    public Mono<ThemaResponse> analyzeThema(String text) {

        return webClientForThema.post()
                .uri("/predict")
                .contentType(MediaType.APPLICATION_JSON)  // 요청 바디 타입
                .bodyValue(Map.of("text", text))
                .retrieve()
                .bodyToMono(ThemaResponse.class);
    }

    public Mono<SummaryResponse> analyzeSummary(String text) {
        return webClientForSummary.post()
                .uri("/summarize")
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(Map.of("text", text))
                .retrieve()
                .bodyToMono(SummaryResponse.class);
    }

    public Mono<SentimentResponse> analyzeSentiment(List<String> titles) {
        log.info("Received /sentiment request with titles: {}", titles);
        return webClientForSentiment.post()
                .uri("/sentiment")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(titles)
                .exchangeToMono(response -> {
                    log.info("FastAPI response status: {}", response.statusCode());
                    if (response.statusCode().is2xxSuccessful()) {
                        return response.bodyToMono(new ParameterizedTypeReference<List<Map<String, Double>>>() {
                        });
                    } else {
                        return response.createException().flatMap(Mono::error);
                    }
                })
                .map(results -> {
                    Double score = results.isEmpty() ? 0.0 : results.get(0).get("score");
                    return new SentimentResponse(score.floatValue());
                })
                .doOnError(e -> log.error("Error in sentiment analysis: ", e))
                .onErrorReturn(new SentimentResponse(0.0f));
    }
}