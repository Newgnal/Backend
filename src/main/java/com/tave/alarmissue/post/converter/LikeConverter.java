package com.tave.alarmissue.post.converter;

import com.tave.alarmissue.post.domain.*;
import com.tave.alarmissue.post.dto.response.LikeResponseDto;
import com.tave.alarmissue.user.domain.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class LikeConverter {

public static LikeResponseDto toLikeResponseDto(Like like){
    return LikeResponseDto.builder()
            .likeId(like.getLikeId())
            .likeType(like.getLikeType())
            .liked(true)
            .build();

}

    public Like toPostLike(UserEntity user, Post post){
        return Like.builder().
                user(user).
                post(post).
                likeType(LikeType.POST).
                liked(true).
                build();
    }
    public Like toCommentLike(UserEntity user, Post post,Comment comment){
        return Like.builder().
                user(user).
                post(post).
                comment(comment).
                likeType(LikeType.COMMENT).
                liked(true).
                build();
    }
    public Like toReplyLike(UserEntity user, Post post, Comment comment, Reply reply){
    return Like.builder().
            user(user).
            post(post).
            comment(comment).
            reply(reply).
            likeType(LikeType.REPLY).
            liked(true).
            build();
    }
}
