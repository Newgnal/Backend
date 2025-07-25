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

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class PostCommentConverter {

    public static CommentResponse toCommentResponseDto(PostComment postComment, Map<Long, Boolean> replyIdToIsLikedMap, List<PostReply> replies, boolean isLiked) {
        Map<Long, Boolean> safeReplyIdToIsLikedMap = replyIdToIsLikedMap == null ? Collections.emptyMap() : replyIdToIsLikedMap;
        return CommentResponse.builder()
                .commentId(postComment.getCommentId())
                .commentContent(postComment.getComment())
                .likeCount(postComment.getLikeCount())
                .nickname(postComment.getUser().getNickName())
                .createdAt(postComment.getCreatedAt())
                .voteType(postComment.getVoteType() != null ? postComment.getVoteType() : null)
                .replies(replies != null ? PostReplyConverter.toReplyResponseDtos(replies,safeReplyIdToIsLikedMap) : null)
                .isLiked(isLiked)
                .build();
    }

//    //댓글 대댓글 부모자식 관계로 변환
//    public static List<CommentResponse> toCommentResponseDtos(List<PostComment> comments,List<PostReply> replies,
//                                                              Map<Long, Boolean> commentIdToIsLikedMap) {
//
//        Map<Long, List<ReplyResponse>> repliesByCommentId = replies.stream()
//                .collect(Collectors.groupingBy(
//                        r -> r.getPostComment().getCommentId(),
//                        Collectors.mapping(PostReplyConverter::toReplyResponseDto,
//                                Collectors.toList())
//                ));
//        return comments.stream()
//                .map(c -> CommentResponse.builder()
//                        .commentId(c.getCommentId())
//                        .commentContent(c.getComment())
//                        .likeCount(c.getLikeCount())
//                        .nickname(c.getUser().getNickName())
//                        .createdAt(c.getCreatedAt())
//                        .voteType(c.getVoteType())
//                        .replies(repliesByCommentId
//                                .getOrDefault(c.getCommentId(), List.of()))
//                        .isLiked(commentIdToIsLikedMap.getOrDefault(c.getCommentId(), false))
//                        .build()
//                )
//                .toList();
//
//
//    }

    public static List<CommentResponse> toCommentResponseDtos(List<PostComment> comments,
                                                              Map<Long, Boolean> commentIdToIsLikedMap,
                                                              Map<Long, Boolean> replyIdToIsLikedMap) {
        return comments.stream()
                .map(c -> CommentResponse.builder()
                        .commentId(c.getCommentId())
                        .commentContent(c.getComment())
                        .likeCount(c.getLikeCount())
                        .nickname(c.getUser().getNickName())
                        .createdAt(c.getCreatedAt())
                        .voteType(c.getVoteType())
                        .replies(PostReplyConverter.toReplyResponseDtos(c.getReplies(),replyIdToIsLikedMap))  // 댓글 엔티티에 포함된 대댓글 리스트 사용
                        .isLiked(commentIdToIsLikedMap.getOrDefault(c.getCommentId(), false))
                        .build())
                .toList();
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
    }
