package com.tave.alarmissue.post.repository;

import com.tave.alarmissue.post.domain.Comment;
import com.tave.alarmissue.post.domain.Like;
import com.tave.alarmissue.post.domain.Post;
import com.tave.alarmissue.user.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;


public interface LikeRepository extends JpaRepository<Like, Long> {
    

    @Query("SELECT l FROM Like l WHERE l.user = :user AND l.post = :post AND l.comment IS NULL")
    Optional<Like> findPostLike(@Param("user") UserEntity user, @Param("post") Post post);


    @Query("SELECT l FROM Like l WHERE l.user = :user AND l.comment = :comment")
    Optional<Like> findCommentLike(@Param("user") UserEntity user, @Param("comment") Comment comment);

    void deleteAllByPost(Post post);
    void deleteAllByComment(Comment comment);
}
