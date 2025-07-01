package com.tave.alarmissue.news.poll.dto.response;

import com.tave.alarmissue.news.poll.domain.NewsVoteType;
import com.tave.alarmissue.vote.domain.VoteType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NewsVoteCountResponse {
    private NewsVoteType newsVoteType;
    private long count;
}
