package com.tave.alarmissue.post.service;

import com.tave.alarmissue.post.domain.*;
import com.tave.alarmissue.post.domain.enums.TargetType;
import com.tave.alarmissue.post.repository.CommentRepository;
import com.tave.alarmissue.post.converter.PostLikeConverter;
import com.tave.alarmissue.post.dto.response.LikeResponse;
import com.tave.alarmissue.post.exception.PostException;
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
import static com.tave.alarmissue.post.exception.PostErrorCode.*;

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

    // 게시글 좋아요
    @Transactional
    public LikeResponse postLike(Long userId, Long postId) {

        UserEntity user = getUserById(userId);
        Post post = getPostById(postId);

        // 기존 좋아요가 존재하는지 확인
        Optional<PostLike> existingLike = likeRepository.findPostLike(user, post);

        if (existingLike.isPresent()) {

            Long LikeId = existingLike.get().getLikeId();

            likeRepository.delete(existingLike.get()); // 좋아요 취소
            postRepository.decrementLikeCount(postId);

            return new LikeResponse(LikeId,false, TargetType.POST);
        }
    else {
            // 없으면 좋아요 생성
            PostLike like = likeConverter.toPostLike(user, post);
            PostLike saved = likeRepository.save(like);
            postRepository.incrementLikeCount(postId);

            return PostLikeConverter.toLikeResponseDto(saved);
        }
    }

    // 댓글 좋아요
    @Transactional
    public LikeResponse commentLike(Long userId,Long commentId) {

        UserEntity user = getUserById(userId);
        PostComment postComment = getPostCommentById(commentId);

        Optional<PostLike> existingLike = likeRepository.findCommentLike(user, postComment);

        if (existingLike.isPresent()) {
            Long LikeId = existingLike.get().getLikeId();
            likeRepository.delete(existingLike.get()); // 좋아요 취소
            commentRepository.decrementLikeCount(commentId);
            return new LikeResponse(LikeId,false,TargetType.COMMENT);
        }

        // 없으면 좋아요 생성
        else {

            PostLike like = likeConverter.toCommentLike(user, postComment);
            PostLike saved = likeRepository.save(like);
            commentRepository.incrementLikeCount(commentId);
            return PostLikeConverter.toLikeResponseDto(saved);
        }
    }


    // 대댓글 좋아요
    @Transactional
    public LikeResponse replyLike(Long userId, Long replyId) {

        UserEntity user = getUserById(userId);
        PostReply reply = getPostReplyById(replyId);

        Optional<PostLike> existingLike = likeRepository.findReplyLike(user,reply);
        if (existingLike.isPresent()) {
            Long LikeId = existingLike.get().getLikeId();
            likeRepository.delete(existingLike.get()); // 좋아요 취소
            replyRepository.decrementLikeCount(replyId);
            return new LikeResponse(LikeId,false,TargetType.REPLY);
        }
        // 없으면 좋아요 생성
        else {
            PostLike like = likeConverter.toReplyLike(user,reply);
            PostLike saved = likeRepository.save(like);
            replyRepository.incrementLikeCount(replyId);
            return PostLikeConverter.toLikeResponseDto(saved);
        }
    }


    /*
    private method 분리
     */
    private UserEntity getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new PostException(USER_ID_NOT_FOUND, "userId"+userId));
    }

    private Post getPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new PostException(POST_ID_NOT_FOUND," postId: " + postId));
    }

    private PostComment getPostCommentById(Long commentId) {

        return commentRepository.findById(commentId).
                orElseThrow(() -> new PostException(COMMENT_ID_NOT_FOUND, " commentId: " + commentId));
    }

    private PostReply getPostReplyById(Long replyId){
        return replyRepository.findById(replyId).
                orElseThrow(() -> new PostException(REPLY_ID_NOT_FOUND," replyId: " + replyId));
    }
}

