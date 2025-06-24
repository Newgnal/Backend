package com.tave.alarmissue.post.dto.response;

import com.tave.alarmissue.post.domain.PostType;
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
    private PostType postType;
    private String nickname;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
