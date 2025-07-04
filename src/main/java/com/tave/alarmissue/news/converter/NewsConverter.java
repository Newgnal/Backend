package com.tave.alarmissue.news.converter;


import com.tave.alarmissue.news.domain.News;
import com.tave.alarmissue.news.dto.response.NewsDetailResponseDto;
import com.tave.alarmissue.news.dto.response.NewsResponseDto;
import org.springframework.stereotype.Component;

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
                .content(news.getContent())
                .source(news.getSource())
                .date(news.getDate())
                .thema(news.getThema())
                .sentiment(news.getSentiment() != null ? news.getSentiment() : 0.0)
                .view(news.getView())
                .imageUrl(news.getImageUrl())
                .build();
    }
}
