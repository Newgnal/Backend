package com.tave.alarmissue.post.converter;

import com.tave.alarmissue.post.domain.PostComment;
import com.tave.alarmissue.post.dto.request.CommentCreateRequest;
import com.tave.alarmissue.post.dto.response.CommentResponse;
import com.tave.alarmissue.post.domain.Post;
import com.tave.alarmissue.user.domain.UserEntity;
import com.tave.alarmissue.post.domain.enums.VoteType;
import org.springframework.stereotype.Component;

@Component
public class PostCommentConverter {
    public static CommentResponse toCommentResponseDto(PostComment postComment) {
        return CommentResponse.builder()
                .commentId(postComment.getCommentId())
            .commentContent(postComment.getCommentContent())
                .likeCount(postComment.getLikeCount())
            .nickname(postComment.getUser().getNickName())
            .createdAt(postComment.getCreatedAt())
                .voteType(postComment.getVoteTypeSnapshot() != null ? postComment.getVoteTypeSnapshot() : null)
                .build();
    }
    public PostComment toComment(CommentCreateRequest dto, UserEntity user, Post post, VoteType voteType) {
        return PostComment.builder()
                .commentContent(dto.getComment())
                .user(user)
                .post(post)
                .voteTypeSnapshot(voteType)
                .likeCount(0L)
                .build();
    }

}
