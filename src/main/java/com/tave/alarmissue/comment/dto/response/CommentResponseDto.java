package com.tave.alarmissue.comment.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class CommentResponseDto {
    private Long commentId;
    private String comment;
    private String nickname;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}