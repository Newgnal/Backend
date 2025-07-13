package com.tave.alarmissue.post.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class PostDetailResponse {
    private PostResponse post;
    private List<CommentResponse> comments;
}
