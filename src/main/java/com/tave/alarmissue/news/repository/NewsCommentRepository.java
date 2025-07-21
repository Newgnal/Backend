package com.tave.alarmissue.news.repository;

import com.tave.alarmissue.news.domain.News;
import com.tave.alarmissue.news.domain.NewsComment;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface NewsCommentRepository extends JpaRepository<NewsComment,Long> {
    //특정 뉴스의 모든 댓글을 최신순으로 조회
    List<NewsComment> findByNewsIdOrderByCreatedAtDesc(Long newsId);
    //특정 뉴스에 달린 댓글 개수 조회
    long countByNewsId(Long newsId);

    Optional<NewsComment> findByIdAndNewsIdAndUserId(Long commentId, Long newsId, Long userId);
    Optional<NewsComment> findByIdAndUserId(Long commentId, Long userId);
    Optional<NewsComment> findByIdAndNewsId(Long commentId, Long newsId);

    // 답글 관련 메서드 추가
    List<NewsComment> findByNewsIdAndParentCommentIsNullOrderByCreatedAtDesc(Long newsId);  // 원댓글만 조회
    List<NewsComment> findByParentCommentIdOrderByCreatedAtDesc(Long parentCommentId);      // 특정 댓글의 답글들
    Long countByParentCommentId(Long parentCommentId);

    List<NewsComment> findByNewsIdAndUserId(Long newsId, Long userId);
}
