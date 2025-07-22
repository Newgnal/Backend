package com.tave.alarmissue.report.repository;

import com.tave.alarmissue.news.domain.NewsComment;
import com.tave.alarmissue.post.domain.PostComment;
import com.tave.alarmissue.post.domain.Post;
import com.tave.alarmissue.post.domain.PostReply;
import com.tave.alarmissue.report.domain.Report;
import com.tave.alarmissue.user.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {


    @Query("SELECT l FROM Report l WHERE l.user = :user AND l.post = :post")
    Optional<Report> findPostReport(@Param("user") UserEntity user, @Param("post") Post post);

    @Query("SELECT l FROM Report l WHERE l.user = :user AND l.postComment = :comment")
    Optional<Report> findCommentReport(@Param("user") UserEntity user, @Param("comment") PostComment postComment);

    @Query("SELECT l FROM Report l WHERE l.user = :user AND l.postReply = :reply")
    Optional<Report> findReplyReport(@Param("user")UserEntity user, @Param("reply") PostReply reply);

    @Query("SELECT l FROM Report l WHERE l.user = :user AND l.newsComment = :newscomment")
    Optional<Report> findNewsReport(@Param("user")UserEntity user, @Param("newscomment") NewsComment newsComment);
}
