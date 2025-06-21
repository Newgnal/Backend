package com.tave.alarmissue.post.dto.request;

import com.tave.alarmissue.post.domain.PostType;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostCreateRequestDto {
    private String postTitle;
    private String postContent;
    private String postImage;
    private PostType postType;
}
