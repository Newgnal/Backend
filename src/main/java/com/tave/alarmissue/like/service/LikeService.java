package com.tave.alarmissue.like.service;

import com.tave.alarmissue.comment.domain.Comment;
import com.tave.alarmissue.comment.repository.CommentRepository;
import com.tave.alarmissue.like.conveter.LikeConverter;
import com.tave.alarmissue.like.domain.Like;
import com.tave.alarmissue.like.domain.LikeType;
import com.tave.alarmissue.like.domain.Liked;
import com.tave.alarmissue.like.dto.response.LikeResponseDto;
import com.tave.alarmissue.like.exception.LikeException;
import com.tave.alarmissue.like.repository.LikeRepository;
import com.tave.alarmissue.post.domain.Post;
import com.tave.alarmissue.post.repository.PostRepository;
import com.tave.alarmissue.user.domain.UserEntity;
import com.tave.alarmissue.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
import static com.tave.alarmissue.like.exception.LikeErrorCode.*;

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
            return new LikeResponseDto(LikeId, Liked.FALSE,LikeType.POST);
        }

        // 없으면 좋아요 생성
        Like like = likeConverter.toPostLike(user, post);
        Like saved = likeRepository.save(like);
        postRepository.incrementLikeCount(postId);
        return LikeConverter.toLikeResponseDto(saved);
    }

    @Transactional
    public LikeResponseDto commentLike( Long userId, Long postId, Long commentId) {
        UserEntity user = userRepository.findById(userId).
                orElseThrow(() -> new LikeException(USER_ID_NOT_FOUND,"유저가 없습니다."));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new LikeException(POST_ID_NOT_FOUND,"postId:" + postId));

        Comment comment = commentRepository.findById(commentId).
                orElseThrow(()->new LikeException(COMMENT_ID_NOT_FOUND," commentId: " + commentId));

        if(!comment.getPost().getPostId().equals(post.getPostId())) {
            throw new LikeException(POST_ID_MIXMATCH,"postId: "+ post.getPostId() +"comment의 postId: "+ comment.getPost().getPostId());
        }


        Optional<Like> existingLike = likeRepository.findCommentLike(user,comment);
        if (existingLike.isPresent()) {
            Long LikeId = existingLike.get().getLikeId();
            likeRepository.delete(existingLike.get()); // 좋아요 취소
            commentRepository.decrementLikeCount(commentId);
            return new LikeResponseDto(LikeId,Liked.FALSE,LikeType.COMMENT);
        }
        // 없으면 좋아요 생성
        Like like = likeConverter.toCommentLike(user, post, comment);
        Like saved = likeRepository.save(like);
        commentRepository.incrementLikeCount(commentId);
        return LikeConverter.toLikeResponseDto(saved);

    }
}
