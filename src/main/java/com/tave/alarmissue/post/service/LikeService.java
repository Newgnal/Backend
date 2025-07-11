package com.tave.alarmissue.post.service;

import com.tave.alarmissue.post.domain.*;
import com.tave.alarmissue.post.repository.CommentRepository;
import com.tave.alarmissue.post.converter.LikeConverter;
import com.tave.alarmissue.post.dto.response.LikeResponseDto;
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
    private final LikeConverter likeConverter;
    private final CommentRepository commentRepository;
    private final ReplyRepository replyRepository;


    @Transactional
    public LikeResponseDto postLike(Long userId, Long postId) {

        UserEntity user = userRepository.findById(userId).
                orElseThrow(() -> new LikeException(USER_ID_NOT_FOUND,"유저가 없습니다."));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new LikeException(POST_ID_NOT_FOUND," postId: " + postId));

        // 기존 좋아요가 존재하는지 확인
        Optional<Like> existingLike = likeRepository.findPostLike(user, post);
        if (existingLike.isPresent()) {
           Long LikeId = existingLike.get().getLikeId();
            likeRepository.delete(existingLike.get()); // 좋아요 취소
            postRepository.decrementLikeCount(postId);
            return new LikeResponseDto(LikeId,false,LikeType.POST);
        }
    else {
            // 없으면 좋아요 생성
            Like like = likeConverter.toPostLike(user, post);
            Like saved = likeRepository.save(like);
            postRepository.incrementLikeCount(postId);
            return LikeConverter.toLikeResponseDto(saved);
        }
    }

    @Transactional
    public LikeResponseDto commentLike(Long userId,Long commentId) {
        UserEntity user = userRepository.findById(userId).
                orElseThrow(() -> new LikeException(USER_ID_NOT_FOUND,"유저가 없습니다."));


        Comment comment = commentRepository.findById(commentId).
                orElseThrow(()->new LikeException(COMMENT_ID_NOT_FOUND," commentId: " + commentId));


        Post post = postRepository.findById(comment.getPost().getPostId())
                .orElseThrow(() -> new LikeException(POST_ID_NOT_FOUND,"postId:" + comment.getPost().getPostId()));

        Optional<Like> existingLike = likeRepository.findCommentLike(user,comment);
        if (existingLike.isPresent()) {
            Long LikeId = existingLike.get().getLikeId();
            likeRepository.delete(existingLike.get()); // 좋아요 취소
            commentRepository.decrementLikeCount(commentId);
            return new LikeResponseDto(LikeId,false,LikeType.COMMENT);
        }
        // 없으면 좋아요 생성
        else {
            Like like = likeConverter.toCommentLike(user, post, comment);
            Like saved = likeRepository.save(like);
            commentRepository.incrementLikeCount(commentId);
            return LikeConverter.toLikeResponseDto(saved);
        }
    }
    @Transactional
    public LikeResponseDto replyLike(Long userId, Long replyId) {
        UserEntity user = userRepository.findById(userId).
                orElseThrow(() -> new LikeException(USER_ID_NOT_FOUND,"유저가 없습니다."));

        Reply reply = replyRepository.findById(replyId).
                orElseThrow(() -> new LikeException(REPLY_ID_NOT_FOUND," replyId: " + replyId));

        Post post = postRepository.findById(reply.getPost().getPostId())
                .orElseThrow(()->new LikeException(POST_ID_NOT_FOUND," postId: " + reply.getPost().getPostId()));

        Comment comment = commentRepository.findById(reply.getComment().getCommentId())
                .orElseThrow(()->new LikeException(COMMENT_ID_NOT_FOUND," commentId: " + reply.getComment().getCommentId()));

        Optional<Like> existingLike = likeRepository.findReplyLike(user,reply);
        if (existingLike.isPresent()) {
            Long LikeId = existingLike.get().getLikeId();
            likeRepository.delete(existingLike.get()); // 좋아요 취소
            replyRepository.decrementLikeCount(replyId);
            return new LikeResponseDto(LikeId,false,LikeType.REPLY);
        }
        // 없으면 좋아요 생성
        else {
            Like like = likeConverter.toReplyLike(user, post, comment, reply);
            Like saved = likeRepository.save(like);
            replyRepository.incrementLikeCount(replyId);
            return LikeConverter.toLikeResponseDto(saved);
        }
    }

}
