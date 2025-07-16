package com.tave.alarmissue.news.dto.response;

import com.tave.alarmissue.news.domain.enums.Thema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class NewsDetailResponseDto {

    private Long id;
    private String title;
    private String contentUrl;
    private String source;
    private Thema thema;
    private LocalDateTime date;
    private double sentiment;
    private String imageUrl;
    private String imageCaption;
    private Long view;
    private Long commentNum;
    private Long voteNum;
    private String summary;
}
