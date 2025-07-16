package com.tave.alarmissue.news.converter;


import com.tave.alarmissue.news.domain.News;
import com.tave.alarmissue.news.dto.response.NewsDetailResponseDto;
import com.tave.alarmissue.news.dto.response.NewsResponseDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class NewsConverter {
    public NewsResponseDto toDto(News news){
        return NewsResponseDto.builder().id(news.getId())
                .title(news.getTitle())
                .source(news.getSource())
                .date(news.getDate())
                .thema(news.getThema())
                .sentiment(news.getSentiment() != null ? news.getSentiment() : 0.0)
                .view(news.getView())
                .imageUrl(news.getImageUrl())
                .build();
    }

    public NewsDetailResponseDto toDetailDto(News news) {
        return NewsDetailResponseDto.builder().id(news.getId())
                .title(news.getTitle())
                .contentUrl(news.getContentUrl())
                .source(news.getSource())
                .date(news.getDate())
                .thema(news.getThema())
                .sentiment(news.getSentiment() != null ? news.getSentiment() : 0.0)
                .view(news.getView())
                .imageUrl(news.getImageUrl())
                .build();
    }

    /**
     * News 엔티티 리스트를 NewsDto 리스트로 변환
     */
    public List<NewsResponseDto> toDtoList(List<News> newsList) {
        if (newsList == null || newsList.isEmpty()) {
            return new ArrayList<>();
        }

        return newsList.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
