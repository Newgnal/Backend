package com.tave.alarmissue.news.poll.dto.response;

import com.tave.alarmissue.news.poll.domain.NewsVoteType;
import com.tave.alarmissue.vote.domain.VoteType;
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

}
