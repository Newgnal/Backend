package com.tave.alarmissue.newsroom.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class KeywordResponse {
    private Long id;
    private String keyword;
    private LocalDateTime createdAt;
    private Long userId;
}