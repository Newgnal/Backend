package com.tave.alarmissue.post.dto.response;

import com.tave.alarmissue.news.domain.enums.Thema;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class HotPostResponse {
    private Long postId;
    private String postTitle;
    private Long likeCount;
    private Thema thema;
    private Long viewCount;
    private Long commentCount;
    private boolean isLiked;
}
