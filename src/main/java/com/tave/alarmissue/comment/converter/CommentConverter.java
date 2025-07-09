package com.tave.alarmissue.comment.converter;

import com.tave.alarmissue.comment.domain.Comment;
import com.tave.alarmissue.comment.dto.request.CommentCreateRequestDto;
import com.tave.alarmissue.comment.dto.response.CommentResponseDto;
import com.tave.alarmissue.post.domain.Post;
import com.tave.alarmissue.user.domain.UserEntity;
import com.tave.alarmissue.vote.domain.Vote;
import com.tave.alarmissue.vote.domain.VoteType;
import org.springframework.stereotype.Component;

@Component
public class CommentConverter {
    public static CommentResponseDto toCommentResponseDto(Comment comment) {
        return CommentResponseDto.builder()
                .commentId(comment.getCommentId())
            .commentContent(comment.getCommentContent())
                .likeCount(comment.getLikeCount())
            .nickname(comment.getUser().getNickName())
            .createdAt(comment.getCreatedAt())
            .updatedAt(comment.getUpdatedAt())
                .voteType(comment.getVoteTypeSnapshot() != null ? comment.getVoteTypeSnapshot() : null)
                .build();
    }
    public Comment toComment(CommentCreateRequestDto dto, UserEntity user, Post post, VoteType voteType) {
        return Comment.builder()
                .commentContent(dto.getCommentContent())
                .user(user)
                .post(post)
                .voteTypeSnapshot(voteType)
                .likeCount(0L)
                .build();
    }

}
