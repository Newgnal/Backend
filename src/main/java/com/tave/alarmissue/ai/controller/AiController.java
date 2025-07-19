package com.tave.alarmissue.ai.controller;

import com.tave.alarmissue.ai.dto.request.SummaryRequest;
import com.tave.alarmissue.ai.dto.request.ThemaRequest;
import com.tave.alarmissue.ai.dto.response.SummaryResponse;
import com.tave.alarmissue.ai.dto.response.ThemaResponse;
import com.tave.alarmissue.ai.service.AiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
public class AiController {

    private final AiService aiService;

    @PostMapping("/thema")
    public Mono<ThemaResponse> analyzeThema(@RequestBody ThemaRequest request) {
        return aiService.analyzeThema(request.getText());
    }

//    @PostMapping("/summary")
//    public Mono<SummaryResponse> analyzeSummary(@RequestBody SummaryRequest request) {
//        return aiService.analyzeSummary(request.getContentUrl());
//    }
}
