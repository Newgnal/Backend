package com.tave.alarmissue.ai.service;

import com.tave.alarmissue.ai.dto.response.SentimentResponse;
import com.tave.alarmissue.ai.dto.response.SummaryResponse;
import com.tave.alarmissue.ai.dto.response.ThemaResponse;
import lombok.RequiredArgsConstructor;
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
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(Map.of("text", text))
                .retrieve()
                .bodyToMono(SummaryResponse.class);
    }

    public Mono<SentimentResponse> analyzeSentiment(String title) {
        // 배열을 직접 전송 (객체로 감싸지 않음)
        List<String> titles = List.of(title);

        return webClient.post()
                .uri("/sentiment")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(titles)  // Map이 아닌 List 직접 전송
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Map<String, Double>>>() {})
                .map(results -> {
                    if (results.isEmpty()) {
                        return new SentimentResponse(0.0f);
                    }
                    Double score = results.get(0).get("score");
                    return new SentimentResponse(score != null ? score.floatValue() : 0.0f);
                })
                .onErrorReturn(new SentimentResponse(0.0f));
    }


}
