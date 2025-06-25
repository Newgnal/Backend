package com.tave.alarmissue.news.repository;

import com.tave.alarmissue.news.domain.News;
import com.tave.alarmissue.news.domain.enums.Thema;

import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface NewsRepository extends JpaRepository<News, Long> {


//    Page<News> findAllByOrderByDateDesc(Pageable pageable);
//    Page<News> findAllByOrderByViewDesc(Pageable pageable);
    List<NewsMapping> findByThema(Thema thema);

    @Override
    List<News> findAll();

    List<NewsMapping> findAllByOrderByDateDesc();

}
