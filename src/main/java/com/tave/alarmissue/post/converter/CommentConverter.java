package com.tave.alarmissue.post.converter;

import com.tave.alarmissue.post.domain.Comment;
import com.tave.alarmissue.post.dto.request.CommentCreateRequestDto;
import com.tave.alarmissue.post.dto.response.CommentResponseDto;
import com.tave.alarmissue.post.domain.Post;
import com.tave.alarmissue.user.domain.UserEntity;
import com.tave.alarmissue.post.domain.VoteType;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CommentConverter {
    public static CommentResponseDto toCommentResponseDto(Comment comment) {
        return CommentResponseDto.builder()
                .commentId(comment.getCommentId())
            .commentContent(comment.getCommentContent())
                .likeCount(comment.getLikeCount())
            .nickname(comment.getUser().getNickName())
            .createdAt(comment.getCreatedAt())
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

    public static List<CommentResponseDto> toCommentResponseDtos(List<Comment> comments) {
        return comments.stream()
                .map(CommentConverter::toCommentResponseDto)
                .collect(Collectors.toList());
    }
}
