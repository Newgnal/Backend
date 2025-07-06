package com.tave.alarmissue.search.repository;

import com.tave.alarmissue.search.domain.Search;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SearchRepository extends JpaRepository<Search, Long> {

    List<Search> findByUserIdOrderByIdDesc(Long userId);

    boolean existsByUserIdAndContent(Long userId, String content);

}
