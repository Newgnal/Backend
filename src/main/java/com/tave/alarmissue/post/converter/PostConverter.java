package com.tave.alarmissue.post.converter;

import com.tave.alarmissue.post.domain.Post;
import com.tave.alarmissue.post.dto.request.PostCreateRequestDto;
import com.tave.alarmissue.post.dto.response.PostResponseDto;
import com.tave.alarmissue.user.domain.UserEntity;

public class PostConverter {

    public static PostResponseDto toPostResponseDto(Post post) {
        return PostResponseDto.builder()
                .postId(post.getPostId())
                .postTitle(post.getPostTitle())
                .postContent(post.getPostContent())
                .postImage(post.getPostImage())
                .postType(post.getPostType())
                .build();
    }

    public Post toPost(PostCreateRequestDto dto, UserEntity user) {
        return Post.builder()
                .postTitle(dto.getPostTitle())
                .postContent(dto.getPostContent())
                .postImage(dto.getPostImage())
                .postType(dto.getPostType())
                .user(user)
                .build();
    }

}
