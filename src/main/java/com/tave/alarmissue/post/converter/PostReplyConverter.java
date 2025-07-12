package com.tave.alarmissue.post.converter;

import com.tave.alarmissue.post.domain.PostComment;
import com.tave.alarmissue.post.domain.Post;
import com.tave.alarmissue.post.domain.PostReply;
import com.tave.alarmissue.post.domain.PostReply;
import com.tave.alarmissue.post.dto.request.ReplyCreateRequest;
import com.tave.alarmissue.post.dto.response.ReplyResponse;
import com.tave.alarmissue.user.domain.UserEntity;
import com.tave.alarmissue.post.domain.enums.VoteType;
import org.springframework.stereotype.Component;

@Component
public class PostReplyConverter {
    public static ReplyResponse toReplyResponseDto(PostReply reply) {
        return ReplyResponse.builder()
                .replyId(reply.getReplyId())
                .replyContent(reply.getReplyContent())
                .nickname(reply.getUser().getNickName())
                .createdAt(reply.getCreatedAt())
                .voteType(reply.getVoteTypeSnapshot() != null ? reply.getVoteTypeSnapshot() : null)
                .likeCount(reply.getLikeCount())
                .build();


    }
   public PostReply toReply(ReplyCreateRequest dto, UserEntity user, Post post, PostComment postComment, VoteType voteType) {
        return PostReply.builder().
                replyContent(dto.getReplyContent()).
                user(user).
                post(post).
                postComment(postComment).
                voteTypeSnapshot(voteType).
                likeCount(0L).
                build();
   }
}
