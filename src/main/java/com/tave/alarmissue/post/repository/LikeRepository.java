package com.tave.alarmissue.post.repository;

import com.tave.alarmissue.post.domain.PostComment;
import com.tave.alarmissue.post.domain.PostLike;
import com.tave.alarmissue.post.domain.Post;
import com.tave.alarmissue.post.domain.PostReply;
import com.tave.alarmissue.post.domain.enums.TargetType;
import com.tave.alarmissue.user.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<PostLike, Long> {
    @Query("SELECT l FROM PostLike l " +
            "WHERE l.user.id = :userId AND l.post.postId = :postId AND l.targetType = 'POST'")
    Optional<PostLike> findPostLike(
            @Param("userId") Long userId,
            @Param("postId") Long postId
    );

    // 댓글 좋아요
    @Query("SELECT l FROM PostLike l " +
            "WHERE l.user.id = :userId AND l.comment.commentId = :commentId AND l.targetType = 'COMMENT'")
    Optional<PostLike> findCommentLike(
            @Param("userId") Long userId,
            @Param("commentId") Long commentId
    );

    // 대댓글 좋아요
    @Query("SELECT l FROM PostLike l " +
            "WHERE l.user.id = :userId AND l.postReply.replyId = :replyId AND l.targetType = 'REPLY'")
    Optional<PostLike> findReplyLike(
            @Param("userId") Long userId,
            @Param("replyId") Long replyId
    );

    @Query("SELECT l FROM PostLike l WHERE l.user = :user AND l.targetType = :targetType AND l.post.postId IN :postIds")
    List<PostLike> findAllByUserAndTargetTypeAndPostIds(
            @Param("user") UserEntity user,
            @Param("targetType") TargetType targetType,
            @Param("postIds") List<Long> postIds);


    @Query("SELECT l FROM PostLike l WHERE l.user = :user AND l.targetType = :targetType AND l.comment.commentId IN :commentIds")
    List<PostLike> findAllByUserAndTargetTypeAndCommentIds(
            @Param("user") UserEntity user,
            @Param("targetType") TargetType targetType,
            @Param("commentIds") List<Long> commentIds);

    @Query("SELECT l FROM PostLike l WHERE l.user = :user AND l.targetType = :targetType AND l.postReply.replyId IN :replyIds")
    List<PostLike> findAllByUserAndTargetTypeAndReplyIds(
            @Param("user") UserEntity user,
            @Param("targetType") TargetType targetType,
            @Param("replyIds") List<Long> replyIds);


}
