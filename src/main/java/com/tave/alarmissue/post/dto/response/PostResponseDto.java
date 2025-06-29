package com.tave.alarmissue.post.dto.response;


import com.tave.alarmissue.news.enums.Thema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class PostResponseDto {
    private Long postId;
    private String postTitle;
    private String postContent;
    private String articleUrl;
    private Thema thema;
    private String nickname;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean hasVote;
}
