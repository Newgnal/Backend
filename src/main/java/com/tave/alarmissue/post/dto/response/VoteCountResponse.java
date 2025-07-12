package com.tave.alarmissue.post.dto.response;

import com.tave.alarmissue.post.domain.enums.VoteType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VoteCountResponse {
    private VoteType voteType;
    private long count;
}
