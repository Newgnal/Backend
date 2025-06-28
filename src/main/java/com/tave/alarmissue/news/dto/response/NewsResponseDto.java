package com.tave.alarmissue.news.dto.response;


import com.tave.alarmissue.news.domain.enums.Thema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor
public class NewsResponseDto {
    private Long id;
    private String title;
    private String source;
    private Thema thema;
    private LocalDateTime date;
    private double sentiment;
    private String imageUrl;
//    private Long view;
}

