package com.tave.alarmissue.vote.dto.request;

import com.tave.alarmissue.vote.domain.VoteType;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class VoteRequestDto {
    private Long postId;
    private VoteType voteType;
}