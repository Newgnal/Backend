package com.tave.alarmissue.news.repository;

import com.tave.alarmissue.news.domain.News;
import com.tave.alarmissue.news.domain.NewsComment;
import com.tave.alarmissue.news.domain.enums.NewsVoteType;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface NewsCommentRepository extends JpaRepository<NewsComment,Long> {
    //특정 뉴스의 모든 댓글을 최신순으로 조회
    List<NewsComment> findByNewsIdOrderByCreatedAtDesc(Long newsId);

    Optional<NewsComment> findByIdAndNewsIdAndUserId(Long commentId, Long newsId, Long userId);
    Optional<NewsComment> findByIdAndUserId(Long commentId, Long userId);


}
