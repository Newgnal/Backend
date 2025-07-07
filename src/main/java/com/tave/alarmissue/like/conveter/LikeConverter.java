package com.tave.alarmissue.like.conveter;

import com.tave.alarmissue.comment.domain.Comment;
import com.tave.alarmissue.like.domain.Like;
import com.tave.alarmissue.like.domain.LikeType;
import com.tave.alarmissue.like.dto.request.LikeCreateRequestDto;
import com.tave.alarmissue.like.dto.response.LikeResponseDto;
import com.tave.alarmissue.post.domain.Post;
import com.tave.alarmissue.user.domain.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class LikeConverter {

public static LikeResponseDto toLikeResponseDto(Like like){
    return LikeResponseDto.builder()
            .likeId(like.getLikeId())
            .liked(like.isLiked())
            .likeType(like.getLikeType())
            .build();

}

    public Like toPostLike(LikeCreateRequestDto dto, UserEntity user, Post post ,LikeType likeType){
        return Like.builder().
                liked(dto.isLiked()).
                user(user).
                post(post).
                likeType(likeType).
                build();
    }
    public Like toCommentLike(LikeCreateRequestDto dto, UserEntity user, Post post,Comment comment,LikeType likeType){
        return Like.builder().
                liked(dto.isLiked()).
                user(user).
                post(post).
                comment(comment).
                likeType(likeType).
                build();
    }
}
