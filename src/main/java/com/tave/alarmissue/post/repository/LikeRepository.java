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
            "WHERE l.user = :user AND l.post = :post AND l.targetType = 'POST'")
    Optional<PostLike> findPostLike(
            @Param("user") UserEntity user,
            @Param("post") Post post
    );

    @Query("SELECT l FROM PostLike l " +
            "WHERE l.user = :user AND l.comment = :comment AND l.targetType = 'COMMENT'")
    Optional<PostLike> findCommentLike(
            @Param("user") UserEntity user,
            @Param("comment") PostComment comment
    );

    @Query("SELECT l FROM PostLike l " +
            "WHERE l.user = :user AND l.postReply = :reply AND l.targetType = 'REPLY'")
    Optional<PostLike> findReplyLike(
            @Param("user") UserEntity user,
            @Param("reply") PostReply reply
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
