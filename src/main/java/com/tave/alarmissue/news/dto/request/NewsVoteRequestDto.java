package com.tave.alarmissue.news.dto.request;

import com.tave.alarmissue.news.domain.NewsVoteType;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NewsVoteRequestDto {

    private Long newsId;
    private NewsVoteType voteType;

}
