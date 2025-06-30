package com.tave.alarmissue.news.repository;

import com.tave.alarmissue.news.domain.News;
import com.tave.alarmissue.news.domain.enums.Thema;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Optional;

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

    Optional<News> findByUrl(String url);
    Optional<News> findByTitle(String title);

}
