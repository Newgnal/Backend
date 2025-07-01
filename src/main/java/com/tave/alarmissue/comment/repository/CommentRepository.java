package com.tave.alarmissue.comment.repository;

import com.tave.alarmissue.comment.domain.Comment;
import com.tave.alarmissue.post.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    void deleteAllByPost(Post post);
    Page<Comment> findByPost_PostId(Long postId, Pageable pageable);

}
