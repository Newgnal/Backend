package com.tave.alarmissue.post.converter;

import com.tave.alarmissue.post.domain.*;
import com.tave.alarmissue.post.domain.enums.TargetType;
import com.tave.alarmissue.post.dto.response.LikeResponse;
import com.tave.alarmissue.user.domain.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class PostLikeConverter {

public static LikeResponse toLikeResponseDto(PostLike like){
    return LikeResponse.builder()
            .likeId(like.getLikeId())
            .targetType(like.getTargetType())
            .liked(true)
            .build();

}

    public PostLike toPostLike(UserEntity user, Post post){
        return PostLike.builder().
                user(user).
                post(post).
                targetType(TargetType.POST).
                liked(true).
                build();
    }
    public PostLike toCommentLike(UserEntity user, PostComment postComment){
        return PostLike.builder().
                user(user).
                comment(postComment).
                targetType(TargetType.COMMENT).
                liked(true).
                build();
    }
    public PostLike toReplyLike(UserEntity user, PostReply postReply){
    return PostLike.builder().
            user(user).
            postReply(postReply).
            targetType(TargetType.REPLY).
            liked(true).
            build();
    }
}
