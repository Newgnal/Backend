package com.tave.alarmissue.news.poll.converter;

import com.tave.alarmissue.news.domain.News;
import com.tave.alarmissue.news.poll.domain.NewsVoteType;
import com.tave.alarmissue.news.poll.dto.response.NewsVoteCountResponse;
import com.tave.alarmissue.news.poll.dto.response.NewsVoteResponseDto;


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
                .stronglyPositiveCount(strongly_positiveCount)
                .positiveCount(positiveCount)
                .neutralCount(neutralCount)
                .negativeCount(negativeCount)
                .stronglyNegativeCount(negativeCount)
                .voteType(newsVoteType)
                .build();
    }
}
