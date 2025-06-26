package com.tave.alarmissue.vote.dto.response;

import com.tave.alarmissue.vote.domain.VoteType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class VoteResponseDto {
    private Long postId;
    private Integer buyCount;
    private Integer holdCount;
    private Integer sellCount;
    private VoteType voteType;
}