package com.tave.alarmissue.comment.converter;

import com.tave.alarmissue.comment.domain.Comment;
import com.tave.alarmissue.comment.dto.request.CommentCreateRequestDto;
import com.tave.alarmissue.comment.dto.response.CommentResponseDto;
import com.tave.alarmissue.post.domain.Post;
import com.tave.alarmissue.user.domain.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class CommentConverter {
    public static CommentResponseDto toCommentResponseDto(Comment comment) {
        return CommentResponseDto.builder()
                .commentId(comment.getCommentId())
            .comment(comment.getComment())
            .nickname(comment.getUser().getNickName())
            .createdAt(comment.getCreatedAt())
            .updatedAt(comment.getUpdatedAt())
            .build();
    }
    public static Comment toComment(CommentCreateRequestDto dto, UserEntity user, Post post) {
        return Comment.builder()
                .comment(dto.getComment())
                .user(user)
                .post(post)
                .build();
    }

}
