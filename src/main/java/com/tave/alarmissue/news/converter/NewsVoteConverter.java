package com.tave.alarmissue.news.converter;

import com.tave.alarmissue.news.domain.News;
import com.tave.alarmissue.news.domain.enums.NewsVoteType;
import com.tave.alarmissue.news.dto.response.NewsVoteCountResponse;
import com.tave.alarmissue.news.dto.response.NewsVoteResponseDto;
import org.springframework.stereotype.Component;


import java.util.List;

public class NewsVoteConverter {

    public static NewsVoteResponseDto toVoteResponseDto(News news, NewsVoteType newsVoteType,
                                                    List<NewsVoteCountResponse> counts) {
        int strongly_positiveCount=0;
        int positiveCount=0;
        int neutralCount=0;
        int negativeCount=0;
        int strongly_negativeCount=0;


        for (NewsVoteCountResponse c : counts) {
            switch (c.getNewsVoteType()) {
                case STRONGLY_POSITIVE -> strongly_positiveCount = (int) c.getCount();
                case POSITIVE -> positiveCount = (int) c.getCount();
                case NEUTRAL -> neutralCount = (int) c.getCount();
                case NEGATIVE -> negativeCount = (int) c.getCount();
                case STRONGLY_NEGATIVE -> strongly_negativeCount = (int) c.getCount();
            }
        }
        return NewsVoteResponseDto.builder()
                .newsId(news.getId())
                .thema(news.getThema())
                .stronglyPositiveCount(strongly_positiveCount)
                .positiveCount(positiveCount)
                .neutralCount(neutralCount)
                .negativeCount(negativeCount)
                .stronglyNegativeCount(strongly_negativeCount)
                .voteType(newsVoteType)
                .build();
    }
}