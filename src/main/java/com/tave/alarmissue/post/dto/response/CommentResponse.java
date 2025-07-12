package com.tave.alarmissue.post.dto.response;

import com.tave.alarmissue.post.domain.enums.VoteType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class CommentResponse {
    private Long commentId;
    private String commentContent;
    private Long likeCount;
    private String nickname;
    private LocalDateTime createdAt;
    private VoteType voteType;
}