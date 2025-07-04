package com.tave.alarmissue.news.dto.response;

import com.tave.alarmissue.news.domain.enums.NewsVoteType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NewsVoteCountResponse {
    private NewsVoteType newsVoteType;
    private long count;
}
