package com.tave.alarmissue.post.dto.response;

import com.tave.alarmissue.post.domain.enums.VoteType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class VoteResponse {
    private Long postId;
    private Integer buyCount;
    private Integer holdCount;
    private Integer sellCount;
    private VoteType voteType;
}