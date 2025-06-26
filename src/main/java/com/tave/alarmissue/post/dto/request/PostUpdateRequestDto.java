package com.tave.alarmissue.post.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostUpdateRequestDto {
    private String postTitle;
    private String postContent;
    private String articleUrl;
    private boolean hasVote;
}
