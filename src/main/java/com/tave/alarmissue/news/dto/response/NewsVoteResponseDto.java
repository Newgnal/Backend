package com.tave.alarmissue.news.dto.response;

import com.tave.alarmissue.news.domain.News;
import com.tave.alarmissue.news.domain.NewsVoteType;
import com.tave.alarmissue.news.domain.enums.Thema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class NewsVoteResponseDto {

        private Long newsId;
        private Integer stronglyPositiveCount;
        private Integer positiveCount;
        private Integer neutralCount;
        private Integer negativeCount;
        private Integer stronglyNegativeCount;
        private NewsVoteType voteType;
//        private String question;
        private Thema thema;

}
