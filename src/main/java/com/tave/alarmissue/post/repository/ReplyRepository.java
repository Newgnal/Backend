package com.tave.alarmissue.post.repository;

import com.tave.alarmissue.post.domain.PostComment;
import com.tave.alarmissue.post.domain.Post;
import com.tave.alarmissue.post.domain.PostReply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReplyRepository extends JpaRepository<PostReply, Long> {

    @Modifying
    @Query("UPDATE PostReply c SET c.likeCount = c.likeCount + 1 WHERE c.replyId = :replyId")
    void incrementLikeCount(@Param("replyId") Long replyId);

    @Modifying
    @Query("UPDATE PostReply c SET c.likeCount = c.likeCount - 1 WHERE c.replyId = :replyId AND c.likeCount > 0")
    void decrementLikeCount(@Param("replyId") Long replyId);
}
