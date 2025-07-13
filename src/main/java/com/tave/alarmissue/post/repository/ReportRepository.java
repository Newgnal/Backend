package com.tave.alarmissue.post.repository;

import com.tave.alarmissue.post.domain.PostComment;
import com.tave.alarmissue.post.domain.Post;
import com.tave.alarmissue.post.domain.PostReport;
import com.tave.alarmissue.user.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReportRepository extends JpaRepository<PostReport, Long> {


    void deleteAllByPost(Post post);
    void deleteAllByPostComment(PostComment postComment);
    @Query("SELECT l FROM PostReport l WHERE l.user = :user AND l.post = :post AND l.postComment IS NULL")
    Optional<PostReport> findPostReport(UserEntity user, Post post);

    @Query("SELECT l FROM PostReport l WHERE l.user = :user AND l.postComment = :comment")
    Optional<PostReport> findCommentReport(UserEntity user, PostComment comment);
}
