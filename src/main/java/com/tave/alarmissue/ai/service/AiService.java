package com.tave.alarmissue.ai.service;

import com.tave.alarmissue.ai.dto.response.SentimentResponse;
import com.tave.alarmissue.ai.dto.response.SummaryResponse;
import com.tave.alarmissue.ai.dto.response.ThemaResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(Map.of("text", text))
                .retrieve()
                .onStatus(HttpStatusCode::isError, response ->
                        response.bodyToMono(String.class)
                                .defaultIfEmpty("Unknown error")
                                .flatMap(body -> {
                                    log.error("Thema API error: {}", body);
                                    return Mono.error(new RuntimeException("Thema API error: " + body));
                                })
                )
                .bodyToMono(ThemaResponse.class)
                .onErrorResume(e -> {
                    log.error("Thema 요청 실패", e);
                    return Mono.just(new ThemaResponse("분석 실패")); // 또는 null, Optional, 에러 DTO 등
                });
    }

    public Mono<SummaryResponse> analyzeSummary(String text) {
        return webClientForSummary.post()
                .uri("/summarize")
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(Map.of("text", text))
                .retrieve()
                .onStatus(HttpStatusCode::isError, response ->
                        response.bodyToMono(String.class)
                                .flatMap(body -> {
                                    log.error("Summary API error: {}", body);
                                    return Mono.error(new RuntimeException("Summary API error: " + body));
                                })
                )
                .bodyToMono(SummaryResponse.class)
                .onErrorResume(e -> {
                    log.error("Summary 요청 실패", e);
                    return Mono.just(new SummaryResponse("요약 실패")); // 필요에 따라 수정
                });
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
                        return response.bodyToMono(String.class)
                                .defaultIfEmpty("Unknown error")
                                .flatMap(body -> {
                                    log.error("Sentiment API error body: {}", body);
                                    return Mono.error(new RuntimeException("Sentiment API error: " + body));
                                });
                    }
                })
                .map(results -> {
                    Double score = results.isEmpty() ? 0.0 : results.get(0).get("score");

                    float roundedScore = new BigDecimal(score)
                            .setScale(2, RoundingMode.HALF_UP)
                            .floatValue();

                    return new SentimentResponse(roundedScore);
                })
                .onErrorResume(e -> {
                    log.error("Sentiment 요청 실패", e);
                    return Mono.just(new SentimentResponse(0.0f)); // 또는 실패 응답 정의
                });
    }
}