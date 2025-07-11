package com.tave.alarmissue.post.converter;

import com.tave.alarmissue.post.domain.Comment;
import com.tave.alarmissue.post.domain.Post;
import com.tave.alarmissue.post.domain.Reply;
import com.tave.alarmissue.post.dto.request.ReplyCreateRequestDto;
import com.tave.alarmissue.post.dto.response.ReplyResponseDto;
import com.tave.alarmissue.user.domain.UserEntity;
import com.tave.alarmissue.post.domain.VoteType;
import org.springframework.stereotype.Component;

@Component
public class ReplyConverter {
    public static ReplyResponseDto toReplyResponseDto(Reply reply) {
        return ReplyResponseDto.builder()
                .replyId(reply.getReplyId())
                .replyContent(reply.getReplyContent())
                .nickname(reply.getUser().getNickName())
                .createdAt(reply.getCreatedAt())
                .voteType(reply.getVoteTypeSnapshot() != null ? reply.getVoteTypeSnapshot() : null)
                .likeCount(reply.getLikeCount())
                .build();


    }
   public Reply toReply(ReplyCreateRequestDto dto, UserEntity user, Post post, Comment comment, VoteType voteType) {
        return Reply.builder().
                replyContent(dto.getReplyContent()).
                user(user).
                post(post).
                comment(comment).
                voteTypeSnapshot(voteType).
                likeCount(0L).
                build();
   }
}
