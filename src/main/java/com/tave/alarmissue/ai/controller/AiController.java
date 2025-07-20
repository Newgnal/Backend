package com.tave.alarmissue.ai.controller;

import com.tave.alarmissue.ai.dto.request.SentimentRequest;
import com.tave.alarmissue.ai.dto.request.SummaryRequest;
import com.tave.alarmissue.ai.dto.request.ThemaRequest;
import com.tave.alarmissue.ai.dto.response.SentimentResponse;
import com.tave.alarmissue.ai.dto.response.SummaryResponse;
import com.tave.alarmissue.ai.dto.response.ThemaResponse;
import com.tave.alarmissue.ai.service.AiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;


@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
@Slf4j
public class AiController {

    private final AiService aiService;

    @PostMapping(value = "/thema", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ThemaResponse> analyzeThema(@RequestBody ThemaRequest request) {

        String cleanText = request.getText().replaceAll("\\r?\\n", " ");

        return extractTextOrError(cleanText)
                .flatMap(aiService::analyzeThema);
    }

    @PostMapping(value = "/summary", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<SummaryResponse> analyzeSummary(@RequestBody SummaryRequest request) {

        String cleanText = request.getText().replaceAll("\\r?\\n", " ");

        return extractTextOrError(cleanText)
                .flatMap(aiService::analyzeSummary);
    }

    @PostMapping("/sentiment")
    public Mono<SentimentResponse> analyzeSentiment(@RequestBody List<String> titles) {
        return aiService.analyzeSentiment(titles);
    }

    /*
    private method 분리
     */
    private <T> Mono<String> extractTextOrError(String text) {
        if (text == null || text.isEmpty()) {
            log.error("Cannot extract text from JSON");
            return Mono.error(new IllegalArgumentException("텍스트 추출 오류"));
        }

        log.info("Successfully extracted text length: {}", text.length());
        return Mono.just(text);
    }

}