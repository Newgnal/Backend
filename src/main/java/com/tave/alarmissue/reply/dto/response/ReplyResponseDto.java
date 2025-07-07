package com.tave.alarmissue.reply.dto.response;

import com.tave.alarmissue.vote.domain.VoteType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class ReplyResponseDto {
    private Long replyId;
    private String replyContent;
    private String nickname;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private VoteType voteType;
}
