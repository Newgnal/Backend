package com.tave.alarmissue.like.repository;

import com.tave.alarmissue.comment.domain.Comment;
import com.tave.alarmissue.like.domain.Like;
import com.tave.alarmissue.post.domain.Post;
import com.tave.alarmissue.user.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByUserAndPost(UserEntity user, Post post);

    Optional<Like> findByUserAndComment(UserEntity user, Comment comment);
}
