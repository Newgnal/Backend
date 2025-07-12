package com.tave.alarmissue.news.repository;

import com.tave.alarmissue.news.domain.NewsComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NewsCommentRepository extends JpaRepository<NewsComment,Long> {
    //특정 뉴스의 모든 댓글을 최신순으로 조회
    List<NewsComment> findByNewsIdOrderByCreatedAtDesc(Long newsId);
    //특정 뉴스에 달린 댓글 개수 조회
    long countByNewsId(Long newsId);

}
