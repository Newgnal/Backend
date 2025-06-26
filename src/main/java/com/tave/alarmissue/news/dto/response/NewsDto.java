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
    private String content;
    private String url;
    private String imageUrl;
    private String source;
   // private Thema thema;
    private LocalDateTime date;
    private double sentiment;
    private Long view;
}