package com.tave.alarmissue.post.converter;

import com.tave.alarmissue.post.domain.PostComment;
import com.tave.alarmissue.post.domain.PostReply;
import com.tave.alarmissue.post.dto.request.CommentCreateRequest;
import com.tave.alarmissue.post.dto.response.CommentResponse;
import com.tave.alarmissue.post.domain.Post;
import com.tave.alarmissue.post.dto.response.ReplyResponse;
import com.tave.alarmissue.user.domain.UserEntity;
import com.tave.alarmissue.post.domain.enums.VoteType;
import org.openqa.selenium.devtools.Reply;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PostCommentConverter {
    public static CommentResponse toCommentResponseDto(PostComment postComment,List<PostReply> replies) {
        return CommentResponse.builder()
                .commentId(postComment.getCommentId())
            .commentContent(postComment.getComment())
                .likeCount(postComment.getLikeCount())
            .nickname(postComment.getUser().getNickName())
            .createdAt(postComment.getCreatedAt())
                .voteType(postComment.getVoteType() != null ? postComment.getVoteType() : null)
                .replies(replies != null ? PostReplyConverter.toReplyResponseDtos(replies) : null)
                .build();
    }
    public PostComment toComment(CommentCreateRequest dto, UserEntity user, Post post, VoteType voteType) {
        return PostComment.builder()
                .comment(dto.getComment())
                .user(user)
                .post(post)
                .voteType(voteType)
                .likeCount(0L)
                .build();
    }
    public static List<CommentResponse> toCommentResponseDtos(List<PostComment> comments) {
        return comments.stream()
                .map(c -> toCommentResponseDto(c, List.of()))
                .collect(Collectors.toList());
    }

}
