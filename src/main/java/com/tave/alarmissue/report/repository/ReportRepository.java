package com.tave.alarmissue.report.repository;

import com.tave.alarmissue.comment.domain.Comment;
import com.tave.alarmissue.post.domain.Post;
import com.tave.alarmissue.report.domain.Report;
import com.tave.alarmissue.user.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {


    void deleteAllByPost(Post post);
    void deleteAllByComment(Comment comment);
    @Query("SELECT l FROM Report l WHERE l.user = :user AND l.post = :post AND l.comment IS NULL")
    Optional<Report> findPostReport(UserEntity user, Post post);

    @Query("SELECT l FROM Report l WHERE l.user = :user AND l.comment = :comment")
    Optional<Report> findCommentReport(UserEntity user, Comment comment);
}
