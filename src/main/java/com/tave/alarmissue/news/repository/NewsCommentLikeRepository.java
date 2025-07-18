package com.tave.alarmissue.news.repository;

import com.tave.alarmissue.news.domain.NewsCommentLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NewsCommentLikeRepository extends JpaRepository<NewsCommentLike, Long> {

    //특정 사용자가 특정 댓글에 좋아요를 눌렀는지 확인
    Optional<NewsCommentLike> findByCommentIdAndUserId(Long commentId, Long userId);

    //특정 댓글의 좋아요 개수
    long countByCommentId(Long commentId);

    boolean existsByCommentIdAndUserId(Long commentId, Long userId);
}
