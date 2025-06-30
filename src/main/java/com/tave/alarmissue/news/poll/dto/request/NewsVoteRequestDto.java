package com.tave.alarmissue.news.poll.dto.request;

import com.tave.alarmissue.vote.domain.VoteType;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NewsVoteRequestDto {

    private Long newsId;
    private VoteType voteType;

}
