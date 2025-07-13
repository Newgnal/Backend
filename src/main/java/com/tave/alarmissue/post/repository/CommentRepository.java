package com.tave.alarmissue.post.repository;

import com.tave.alarmissue.post.domain.PostComment;
import com.tave.alarmissue.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<PostComment, Long> {


    @Modifying
    @Query("UPDATE PostComment c SET c.likeCount = c.likeCount + 1 WHERE c.commentId = :commentId")
    void incrementLikeCount(@Param("commentId") Long commentId);

    @Modifying
    @Query("UPDATE PostComment c SET c.likeCount = c.likeCount - 1 WHERE c.commentId = :commentId AND c.likeCount > 0")
    void decrementLikeCount(@Param("commentId") Long commentId);

    List<PostComment> findAllByPost(Post post);
}
