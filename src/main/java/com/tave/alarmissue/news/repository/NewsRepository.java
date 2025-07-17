package com.tave.alarmissue.news.repository;

import com.tave.alarmissue.news.domain.News;
import com.tave.alarmissue.news.domain.enums.NewsVoteType;
import com.tave.alarmissue.news.domain.enums.Thema;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {

    @Override
    List<News> findAll();

    Slice<News> findAllByOrderByDateDesc(Pageable pageable);
    Slice<News> findAllByOrderByViewDesc(Pageable pageable);

    Slice<News> findByThemaOrderByDateDesc(Thema thema,Pageable pageable);
    Slice<News> findByThemaOrderByViewDesc(Thema thema,Pageable pageable);

    // 키워드 포함 뉴스 개수
    @Query("SELECT COUNT(n) FROM News n WHERE LOWER(n.title) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    long countByKeyword(@Param("keyword") String keywordText);
    // 첫 페이지 조회
    @Query("SELECT n FROM News n WHERE LOWER(n.title) LIKE LOWER(CONCAT('%', :keyword, '%')) ORDER BY n.id DESC")
    List<News> findByKeywordOrderByIdDesc(@Param("keyword") String keyword, Pageable pageable);

    // 다음 페이지 조회 (lastId 이후)
    @Query("SELECT n FROM News n WHERE LOWER(n.title) LIKE LOWER(CONCAT('%', :keyword, '%')) AND n.id < :lastId ORDER BY n.id DESC")
    List<News> findByKeywordAndIdLessThanOrderByIdDesc(@Param("keyword") String keyword,
                                                       @Param("lastId") Long lastId,
                                                       Pageable pageable);

    Optional<News> findByUrl(String url);
    Optional<News> findByTitle(String title);

    List<News> findByTitleContainingIgnoreCase(String title);


    @Query("SELECT n FROM News n WHERE LOWER(n.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "ORDER BY COALESCE(n.view, 0) DESC, n.date DESC")
    List<News> findTopNewsByKeyword(String keyword, Pageable pageable);

    int deleteByDateBefore(LocalDateTime date);

    @Query("SELECT n.title FROM News n WHERE n.title IN :titles")
    List<String> findAllTitlesByTitleIn(@Param("titles") List<String> titles);

}
