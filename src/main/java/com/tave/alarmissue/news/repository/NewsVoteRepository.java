package com.tave.alarmissue.news.repository;

import com.tave.alarmissue.news.domain.News;
import com.tave.alarmissue.news.domain.NewsVote;
import com.tave.alarmissue.news.domain.enums.NewsVoteType;
import com.tave.alarmissue.news.dto.response.NewsVoteCountResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface NewsVoteRepository extends JpaRepository<NewsVote, Long> {
    Optional<NewsVote> findByNewsId(Long newsId);
//    Optional<NewsVoteType> findVoteTypeByNewsIdAndUserId(Long newsId, Long userId);

    @Query("SELECT v.voteType FROM NewsVote v WHERE v.news.id = :newsId AND v.user.id = :userId")
    Optional<NewsVoteType> findVoteTypeByNewsIdAndUserId(@Param("newsId") Long newsId, @Param("userId") Long userId);

    @Query("SELECT v.voteType, COUNT(v) FROM NewsVote v WHERE v.news = :news GROUP BY v.voteType")
    List<NewsVoteCountResponse> countVotesByType(@Param("news") News news);

    @Query("SELECT nv FROM NewsVote nv WHERE nv.news.id = :newsId AND nv.user.id IN :userIds")
    List<NewsVote> findVoteTypesByNewsIdAndUserIds(@Param("newsId") Long newsId, @Param("userIds") List<Long> userIds);

}
