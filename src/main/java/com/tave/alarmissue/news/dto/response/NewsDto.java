package com.tave.alarmissue.news.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class NewsDto {
    private Long id;
    private String title;
//    private Thema thema;
    private String source;
    private double sentiment;
    private LocalDateTime date;
}