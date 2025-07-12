package com.tave.alarmissue.post.converter;

import com.tave.alarmissue.post.domain.*;
import com.tave.alarmissue.post.domain.enums.LikeType;
import com.tave.alarmissue.post.dto.response.LikeResponse;
import com.tave.alarmissue.user.domain.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class PostLikeConverter {

public static LikeResponse toLikeResponseDto(PostLike like){
    return LikeResponse.builder()
            .likeId(like.getLikeId())
            .likeType(like.getLikeType())
            .liked(true)
            .build();

}

    public PostLike toPostLike(UserEntity user, Post post){
        return PostLike.builder().
                user(user).
                post(post).
                likeType(LikeType.POST).
                liked(true).
                build();
    }
    public PostLike toCommentLike(UserEntity user, Post post, PostComment postComment){
        return PostLike.builder().
                user(user).
                post(post).
                comment(postComment).
                likeType(LikeType.COMMENT).
                liked(true).
                build();
    }
    public PostLike toReplyLike(UserEntity user, Post post, PostComment postComment, PostReply postReply){
    return PostLike.builder().
            user(user).
            post(post).
            comment(postComment).
            postReply(postReply).
            likeType(LikeType.REPLY).
            liked(true).
            build();
    }
}
