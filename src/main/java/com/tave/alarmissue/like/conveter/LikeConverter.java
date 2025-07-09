package com.tave.alarmissue.like.conveter;

import com.tave.alarmissue.comment.domain.Comment;
import com.tave.alarmissue.like.domain.Like;
import com.tave.alarmissue.like.domain.LikeType;
import com.tave.alarmissue.like.domain.Liked;
import com.tave.alarmissue.like.dto.response.LikeResponseDto;
import com.tave.alarmissue.post.domain.Post;
import com.tave.alarmissue.user.domain.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class LikeConverter {

public static LikeResponseDto toLikeResponseDto(Like like){
    return LikeResponseDto.builder()
            .likeId(like.getLikeId())
            .liked(Liked.TRUE)
            .likeType(like.getLikeType())
            .build();

}

    public Like toPostLike(UserEntity user, Post post){
        return Like.builder().
                liked(Liked.TRUE).
                user(user).
                post(post).
                likeType(LikeType.POST).
                build();
    }
    public Like toCommentLike(UserEntity user, Post post,Comment comment){
        return Like.builder().
                liked(Liked.TRUE).
                user(user).
                post(post).
                comment(comment).
                likeType(LikeType.COMMENT).
                build();
    }
}
