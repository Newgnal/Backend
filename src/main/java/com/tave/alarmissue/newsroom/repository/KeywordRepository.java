package com.tave.alarmissue.newsroom.repository;

import com.tave.alarmissue.newsroom.entity.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface KeywordRepository extends JpaRepository<Keyword, Long> {
    boolean existsByUserIdAndKeyword(Long userId, String keyword);


    int countByUserId(Long userId);

    @Modifying
    @Query("UPDATE Keyword k SET k.displayOrder = :displayOrder WHERE k.id = :keywordId")
    void updateDisplayOrder(@Param("keywordId") Long keywordId, @Param("displayOrder") Integer displayOrder);

    @Query("SELECT k FROM Keyword k WHERE k.user.id = :userId ORDER BY k.displayOrder ASC, k.createdAt ASC")
    List<Keyword> findByUserIdOrderByDisplayOrder(@Param("userId") Long userId);

    //알림 활성화된 특정 키워드 조회
    List<Keyword> findByKeywordAndNotificationEnabledTrue(String keyword);

    //알림 활성화된 모든 키워드 조회
    @Query("SELECT DISTINCT k.keyword FROM Keyword k WHERE k.notificationEnabled = true")
    List<String> findAllActiveKeywords();
}

