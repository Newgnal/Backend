package com.tave.alarmissue.post.service;

import com.tave.alarmissue.post.domain.*;
import com.tave.alarmissue.post.domain.enums.LikeType;
import com.tave.alarmissue.post.repository.CommentRepository;
import com.tave.alarmissue.post.converter.PostLikeConverter;
import com.tave.alarmissue.post.dto.response.LikeResponse;
import com.tave.alarmissue.post.exception.LikeException;
import com.tave.alarmissue.post.repository.LikeRepository;
import com.tave.alarmissue.post.repository.PostRepository;
import com.tave.alarmissue.post.repository.ReplyRepository;
import com.tave.alarmissue.user.domain.UserEntity;
import com.tave.alarmissue.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
import static com.tave.alarmissue.post.exception.LikeErrorCode.*;

@RequiredArgsConstructor
@Service
@Slf4j
@Transactional(readOnly = true)
public class LikeService {

    private final UserRepository userRepository;
    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final PostLikeConverter likeConverter;
    private final CommentRepository commentRepository;
    private final ReplyRepository replyRepository;


    @Transactional
    public LikeResponse postLike(Long userId, Long postId) {

        UserEntity user = userRepository.findById(userId).
                orElseThrow(() -> new LikeException(USER_ID_NOT_FOUND,"유저가 없습니다."));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new LikeException(POST_ID_NOT_FOUND," postId: " + postId));

        // 기존 좋아요가 존재하는지 확인
        Optional<PostLike> existingLike = likeRepository.findPostLike(user, post);
        if (existingLike.isPresent()) {
           Long LikeId = existingLike.get().getLikeId();
            likeRepository.delete(existingLike.get()); // 좋아요 취소
            postRepository.decrementLikeCount(postId);
            return new LikeResponse(LikeId,false, LikeType.POST);
        }
    else {
            // 없으면 좋아요 생성
            PostLike like = likeConverter.toPostLike(user, post);
            PostLike saved = likeRepository.save(like);
            postRepository.incrementLikeCount(postId);
            return PostLikeConverter.toLikeResponseDto(saved);
        }
    }

    @Transactional
    public LikeResponse commentLike(Long userId,Long commentId) {
        UserEntity user = userRepository.findById(userId).
                orElseThrow(() -> new LikeException(USER_ID_NOT_FOUND,"유저가 없습니다."));


        PostComment postComment = commentRepository.findById(commentId).
                orElseThrow(()->new LikeException(COMMENT_ID_NOT_FOUND," commentId: " + commentId));


        Post post = postRepository.findById(postComment.getPost().getPostId())
                .orElseThrow(() -> new LikeException(POST_ID_NOT_FOUND,"postId:" + postComment.getPost().getPostId()));

        Optional<PostLike> existingLike = likeRepository.findCommentLike(user, postComment);
        if (existingLike.isPresent()) {
            Long LikeId = existingLike.get().getLikeId();
            likeRepository.delete(existingLike.get()); // 좋아요 취소
            commentRepository.decrementLikeCount(commentId);
            return new LikeResponse(LikeId,false,LikeType.COMMENT);
        }
        // 없으면 좋아요 생성
        else {
            PostLike like = likeConverter.toCommentLike(user, post, postComment);
            PostLike saved = likeRepository.save(like);
            commentRepository.incrementLikeCount(commentId);
            return PostLikeConverter.toLikeResponseDto(saved);
        }
    }
    @Transactional
    public LikeResponse replyLike(Long userId, Long replyId) {
        UserEntity user = userRepository.findById(userId).
                orElseThrow(() -> new LikeException(USER_ID_NOT_FOUND,"유저가 없습니다."));

        PostReply reply = replyRepository.findById(replyId).
                orElseThrow(() -> new LikeException(REPLY_ID_NOT_FOUND," replyId: " + replyId));

        Post post = postRepository.findById(reply.getPost().getPostId())
                .orElseThrow(()->new LikeException(POST_ID_NOT_FOUND," postId: " + reply.getPost().getPostId()));

        PostComment postComment = commentRepository.findById(reply.getPostComment().getCommentId())
                .orElseThrow(()->new LikeException(COMMENT_ID_NOT_FOUND," commentId: " + reply.getPostComment().getCommentId()));

        Optional<PostLike> existingLike = likeRepository.findReplyLike(user,reply);
        if (existingLike.isPresent()) {
            Long LikeId = existingLike.get().getLikeId();
            likeRepository.delete(existingLike.get()); // 좋아요 취소
            replyRepository.decrementLikeCount(replyId);
            return new LikeResponse(LikeId,false,LikeType.REPLY);
        }
        // 없으면 좋아요 생성
        else {
            PostLike like = likeConverter.toReplyLike(user, post, postComment, reply);
            PostLike saved = likeRepository.save(like);
            replyRepository.incrementLikeCount(replyId);
            return PostLikeConverter.toLikeResponseDto(saved);
        }
    }

}
