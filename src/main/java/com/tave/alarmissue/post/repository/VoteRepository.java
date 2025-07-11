package com.tave.alarmissue.post.repository;

import com.tave.alarmissue.post.domain.Post;
import com.tave.alarmissue.user.domain.UserEntity;
import com.tave.alarmissue.post.domain.Vote;
import com.tave.alarmissue.post.dto.response.VoteCountResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface VoteRepository extends JpaRepository<Vote, Long> {

    Optional<Vote> findByUserAndPost(UserEntity currentUser, Post post);

    @Query("SELECT v.voteType, COUNT(v) FROM Vote v WHERE v.post = :post GROUP BY v.voteType")
    List<VoteCountResponse> countVotesByType(@Param("post") Post post);

    void deleteByUserAndPost(UserEntity currentUser, Post post);

    void deleteAllByPost(Post post);

}
