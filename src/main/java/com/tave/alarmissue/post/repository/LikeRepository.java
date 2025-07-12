package com.tave.alarmissue.post.repository;

import com.tave.alarmissue.post.domain.PostComment;
import com.tave.alarmissue.post.domain.PostLike;
import com.tave.alarmissue.post.domain.Post;
import com.tave.alarmissue.post.domain.PostReply;
import com.tave.alarmissue.user.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;


public interface LikeRepository extends JpaRepository<PostLike, Long> {

    @Query("SELECT l FROM PostLike l WHERE l.user = :user AND l.post = :post AND l.comment IS NULL AND l.postReply IS NULL")
    Optional<PostLike> findPostLike(@Param("user") UserEntity user, @Param("post") Post post);

    @Query("SELECT l FROM PostLike l WHERE l.user = :user AND l.comment = :comment AND l.postReply IS NULL")
    Optional<PostLike> findCommentLike(@Param("user") UserEntity user, @Param("comment") PostComment postComment);

    @Query("SELECT l FROM PostLike l WHERE l.user = :user AND l.postReply = :reply")
    Optional<PostLike> findReplyLike(@Param("user")UserEntity user, @Param("reply") PostReply reply);

    void deleteAllByComment(PostComment postComment);

}
