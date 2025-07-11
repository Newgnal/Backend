package com.tave.alarmissue.post.repository;

import com.tave.alarmissue.post.domain.Comment;
import com.tave.alarmissue.post.domain.Post;
import com.tave.alarmissue.post.domain.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReplyRepository extends JpaRepository<Reply, Long> {
    void deleteAllByComment(Comment comment);

    void deleteAllByPost(Post post);

    @Modifying
    @Query("UPDATE Reply c SET c.likeCount = c.likeCount + 1 WHERE c.replyId = :replyId")
    void incrementLikeCount(@Param("replyId") Long replyId);

    @Modifying
    @Query("UPDATE Reply c SET c.likeCount = c.likeCount - 1 WHERE c.replyId = :replyId AND c.likeCount > 0")
    void decrementLikeCount(@Param("replyId") Long replyId);
}
