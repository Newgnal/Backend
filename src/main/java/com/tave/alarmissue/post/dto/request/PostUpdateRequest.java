package com.tave.alarmissue.post.dto.request;

import com.tave.alarmissue.news.domain.enums.Thema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostUpdateRequest {
    private String postTitle;
    private String postContent;
    private String articleUrl;
    private Thema thema;
    private boolean hasVote;
}
