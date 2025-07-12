package com.tave.alarmissue.post.dto.response;

import com.tave.alarmissue.post.domain.enums.VoteType;
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
    private VoteType voteType;
    private Long likeCount;
}
