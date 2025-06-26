package com.tave.alarmissue.vote.dto.response;

import com.tave.alarmissue.vote.domain.VoteType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VoteCountResponse {
    private VoteType voteType;
    private long count;
}
