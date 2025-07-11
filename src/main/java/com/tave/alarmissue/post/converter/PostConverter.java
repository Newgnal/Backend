package com.tave.alarmissue.post.converter;

import com.tave.alarmissue.post.domain.Post;
import com.tave.alarmissue.post.dto.request.PostCreateRequest;
import com.tave.alarmissue.post.dto.response.PostResponse;
import com.tave.alarmissue.user.domain.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class
PostConverter {

    public static PostResponse toPostResponseDto(Post post) {
        return PostResponse.builder()
                .postId(post.getPostId())
                .postTitle(post.getPostTitle())
                .postContent(post.getPostContent())
                .likeCount(post.getLikeCount())
                .articleUrl(post.getArticleUrl())
                .thema(post.getThema())
                .nickname(post.getUser().getNickName())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .hasVote(post.getHasVote())
                .build();
    }

    public Post toPost(PostCreateRequest dto, UserEntity user) {
        return Post.builder()
                .postTitle(dto.getPostTitle())
                .postContent(dto.getPostContent())
                .articleUrl(dto.getArticleUrl())
                .thema(dto.getThema())
                .hasVote(dto.isHasVote())
                .likeCount(0L)
                .user(user)
                .build();
    }

}
