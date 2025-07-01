package com.tave.alarmissue.news.poll.repository;

import com.tave.alarmissue.news.domain.News;
import com.tave.alarmissue.news.poll.domain.NewsVote;
import com.tave.alarmissue.news.poll.dto.response.NewsVoteCountResponse;
import com.tave.alarmissue.post.domain.Post;
import com.tave.alarmissue.vote.dto.response.VoteCountResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface NewsVoteRepository extends JpaRepository<NewsVote, Long> {
    Optional<NewsVote> findByNewsId(Long newsId);

    @Query("SELECT v.voteType, COUNT(v) FROM NewsVote v WHERE v.news = :news GROUP BY v.voteType")
    List<NewsVoteCountResponse> countVotesByType(@Param("news") News news);

}
