package com.tave.alarmissue.post.converter;

import com.tave.alarmissue.post.domain.PostComment;
import com.tave.alarmissue.post.domain.Post;
import com.tave.alarmissue.post.domain.PostReply;
import com.tave.alarmissue.post.domain.PostReply;
import com.tave.alarmissue.post.dto.request.ReplyCreateRequest;
import com.tave.alarmissue.post.dto.response.CommentResponse;
import com.tave.alarmissue.post.dto.response.ReplyResponse;
import com.tave.alarmissue.user.domain.UserEntity;
import com.tave.alarmissue.post.domain.enums.VoteType;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class PostReplyConverter {
    public static ReplyResponse toReplyResponseDto(PostReply reply,boolean isLiked) {
        return ReplyResponse.builder()
                .replyId(reply.getReplyId())
                .replyContent(reply.getReplyContent())
                .nickname(reply.getUser().getNickName())
                .createdAt(reply.getCreatedAt())
                .voteType(reply.getVoteType() != null ? reply.getVoteType() : null)
                .likeCount(reply.getLikeCount())
                .isLiked(isLiked)
                .build();


    }
   public PostReply toReply(ReplyCreateRequest dto, UserEntity user, Post post, PostComment postComment, VoteType voteType) {
        return PostReply.builder().
                replyContent(dto.getReplyContent()).
                user(user).
                post(post).
                postComment(postComment).
                voteType(voteType).
                likeCount(0L).
                build();
   }
    public static List<ReplyResponse> toReplyResponseDtos(List<PostReply> replies, Map<Long, Boolean> replyIdToIsLikedMap) {
        if (replies == null) {
            return Collections.emptyList();
        }
        return replies.stream()
                .map(reply -> PostReplyConverter.toReplyResponseDto(reply, replyIdToIsLikedMap.getOrDefault(reply.getReplyId(), false)))
                .collect(Collectors.toList());
    }
}
