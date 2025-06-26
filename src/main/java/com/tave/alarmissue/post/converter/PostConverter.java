package com.tave.alarmissue.post.converter;

import com.tave.alarmissue.post.domain.Post;
import com.tave.alarmissue.post.dto.request.PostCreateRequestDto;
import com.tave.alarmissue.post.dto.response.PostResponseDto;
import com.tave.alarmissue.user.domain.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class
PostConverter {

    public static PostResponseDto toPostResponseDto(Post post) {
        return PostResponseDto.builder()
                .postId(post.getPostId())
                .postTitle(post.getPostTitle())
                .postContent(post.getPostContent())
                .articleUrl(post.getArticleUrl())
                .nickname(post.getUser().getNickName())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .hasVote(post.getHasVote())
                .build();
    }

    public Post toPost(PostCreateRequestDto dto, UserEntity user) {
        return Post.builder()
                .postTitle(dto.getPostTitle())
                .postContent(dto.getPostContent())
                .articleUrl(dto.getArticleUrl())
                .hasVote(dto.isHasVote())
                .user(user)
                .build();
    }

}
