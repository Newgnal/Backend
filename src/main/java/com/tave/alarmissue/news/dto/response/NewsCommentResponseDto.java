package com.tave.alarmissue.news.dto.response;

import com.tave.alarmissue.news.domain.enums.NewsVoteType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class NewsCommentResponseDto {
    private Long newsId;
    private String commentContent;
    private NewsVoteType voteType;
    private String nickName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
