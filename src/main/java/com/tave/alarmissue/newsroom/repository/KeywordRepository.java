package com.tave.alarmissue.newsroom.repository;

import com.tave.alarmissue.newsroom.entity.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface KeywordRepository extends JpaRepository<Keyword, Long> {
    boolean existsByUserIdAndKeyword(Long userId, String keyword);

    @Query("SELECT k FROM Keyword k WHERE k.user.id = :userId ORDER BY k.createdAt ASC ")
    List<Keyword> findByUserIdOrderByCreatedAtAsc(@Param("userId") Long userId);

    int countByUserId(Long userId);
}

