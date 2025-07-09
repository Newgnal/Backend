package com.tave.alarmissue.post.dto.request;

import com.tave.alarmissue.post.domain.VoteType;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class VoteRequestDto {
    private Long postId;
    private VoteType voteType;
}