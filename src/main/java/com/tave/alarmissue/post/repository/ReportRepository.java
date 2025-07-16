package com.tave.alarmissue.post.repository;

import com.tave.alarmissue.post.domain.PostComment;
import com.tave.alarmissue.post.domain.Post;
import com.tave.alarmissue.post.domain.PostReply;
import com.tave.alarmissue.post.domain.PostReport;
import com.tave.alarmissue.user.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReportRepository extends JpaRepository<PostReport, Long> {


    void deleteAllByPost(Post post);
    void deleteAllByPostComment(PostComment postComment);
    @Query("SELECT l FROM PostReport l WHERE l.user = :user AND l.post = :post")
    Optional<PostReport> findPostReport(@Param("user") UserEntity user, @Param("post") Post post);

    @Query("SELECT l FROM PostReport l WHERE l.user = :user AND l.postComment = :comment")
    Optional<PostReport> findCommentReport(@Param("user") UserEntity user, @Param("comment") PostComment postComment);

    @Query("SELECT l FROM PostReport l WHERE l.user = :user AND l.postReply = :reply")
    Optional<PostReport> findReplyReport(@Param("user")UserEntity user, @Param("reply") PostReply reply);
}
