package com.tave.alarmissue.news.repository;

import com.tave.alarmissue.news.domain.News;
import com.tave.alarmissue.news.domain.enums.Thema;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {

//    List<News> findAllByOrderByDateDesc();
//    List<News> findAllByOrderByViewDesc();
//    List<News> findByThemaOrderByDateDesc(Thema thema);
//    List<News> findByThemaOrderByViewDesc(Thema thema);

    @Override
    List<News> findAll();

    Slice<News> findAllByOrderByDateDesc(Pageable pageable);
    Slice<News> findAllByOrderByViewDesc(Pageable pageable);

    Slice<News> findByThemaOrderByDateDesc(Thema thema,Pageable pageable);
    Slice<News> findByThemaOrderByViewDesc(Thema thema,Pageable pageable);


    // 키워드로 제목 검색 (최신순)
    @Query("SELECT n FROM News n WHERE LOWER(n.title) LIKE LOWER(CONCAT('%', :keyword, '%')) ORDER BY n.date DESC")
    List<News> findByTitleContainingIgnoreCaseOrderByDateDesc(@Param("keyword") String keyword);

    // 키워드로 제목 검색 (조회수순)
    @Query("SELECT n FROM News n WHERE LOWER(n.title) LIKE LOWER(CONCAT('%', :keyword, '%')) ORDER BY n.view DESC")
    List<News> findByTitleContainingIgnoreCaseOrderByViewDesc(@Param("keyword") String keyword);

    // 키워드 포함 뉴스 개수
    @Query("SELECT COUNT(n) FROM News n WHERE LOWER(n.title) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    int countByTitleContainingIgnoreCase(@Param("keyword") String keyword);

}
