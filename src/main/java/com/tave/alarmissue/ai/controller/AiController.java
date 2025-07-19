package com.tave.alarmissue.ai.controller;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tave.alarmissue.ai.dto.request.SentimentRequest;
import com.tave.alarmissue.ai.dto.request.SummaryRequest;
import com.tave.alarmissue.ai.dto.request.ThemaRequest;
import com.tave.alarmissue.ai.dto.response.SentimentResponse;
import com.tave.alarmissue.ai.dto.response.SummaryResponse;
import com.tave.alarmissue.ai.dto.response.ThemaResponse;
import com.tave.alarmissue.ai.service.AiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
@Slf4j
public class AiController {

    private final AiService aiService;
    private final ObjectMapper safeObjectMapper;

    @PostMapping(value = "/thema", consumes = "application/json; charset=UTF-8")
    public Mono<ThemaResponse> analyzeThema(@RequestBody String rawJson) {
        try {
            // 정규식으로 "text" 필드 값 직접 추출
            String text = extractTextFromJson(rawJson);

            if (text == null || text.isEmpty()) {
                log.error("Cannot extract text from JSON");
                return Mono.just(new ThemaResponse("텍스트 추출 오류"));
            }

            log.info("Successfully extracted text length: {}", text.length());
            return aiService.analyzeThema(text);

        } catch (Exception e) {
            log.error("Error processing request: ", e);
            return Mono.just(new ThemaResponse("처리 오류"));
        }
    }

    @PostMapping("/summary")
    public Mono<SummaryResponse> analyzeSummary(@RequestBody SummaryRequest request) {
        return aiService.analyzeSummary(request.getText());
    }

    @PostMapping("/sentiment")
    public Mono<SentimentResponse> analyzeSentiment(@RequestBody SentimentRequest request) {
        return aiService.analyzeSentiment(request.getTitle());
    }

    private String extractTextFromJson(String jsonString) {
        try {
            // "text": "..." 패턴으로 텍스트 추출
            Pattern pattern = Pattern.compile("\"text\"\\s*:\\s*\"([^\"\\\\]*(\\\\.[^\"\\\\]*)*)\"", Pattern.DOTALL);
            Matcher matcher = pattern.matcher(jsonString);

            if (matcher.find()) {
                String extracted = matcher.group(1);
                // 이스케이프된 문자들을 원래대로 변환
                return extracted
                        .replace("\\n", "\n")
                        .replace("\\r", "\r")
                        .replace("\\t", "\t")
                        .replace("\\\"", "\"")
                        .replace("\\\\", "\\");
            }

            return null;
        } catch (Exception e) {
            log.error("Error extracting text from JSON", e);
            return null;
        }
    }
}