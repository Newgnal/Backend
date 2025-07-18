package com.tave.alarmissue.post.dto.response;


import com.tave.alarmissue.news.domain.enums.Thema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class PostResponse {
    private Long postId;
    private String postTitle;
    private String postContent;
    private Long newsId;
    private Long likeCount;
    private Thema thema;
    private String nickname;
    private String imageUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean hasVote;
    private Long viewCount;
    private Long commentCount;
}
