package com.tave.alarmissue.ai.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class SentimentRequest {
    private List<String> titles;
}
