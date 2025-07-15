package com.tave.alarmissue.post.repository;

import com.tave.alarmissue.news.domain.enums.Thema;
import com.tave.alarmissue.post.domain.Post;
import com.tave.alarmissue.post.dto.response.ThemeCountResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @Modifying
    @Query("UPDATE Post p SET p.likeCount = p.likeCount + 1 WHERE p.postId = :postId")
    void incrementLikeCount(@Param("postId") Long postId);

    @Modifying
    @Query("UPDATE Post p SET p.likeCount = p.likeCount - 1 WHERE p.postId = :postId AND p.likeCount > 0")
    void decrementLikeCount(@Param("postId") Long postId);

    @Modifying
    @Query("UPDATE Post p SET p.commentCount = p.commentCount + 1 WHERE p.postId = :postId")
    void incrementCommentCount(@Param("postId") Long postId);

    @Modifying
    @Query("UPDATE Post p SET p.commentCount = p.commentCount - 1 WHERE p.postId = :postId AND p.commentCount > 0")
    void decrementCommentCount(@Param("postId") Long postId);

    @Modifying
    @Query("UPDATE Post p SET p.viewCount = p.viewCount + 1 WHERE p.postId = :postId")
    void incrementViewCount(@Param("postId") Long postId);

    Page<Post> findAllByThema(Thema thema, Pageable pageable);

    @Query("""
       SELECT new com.tave.alarmissue.post.dto.response.ThemeCountResponse(
                  p.thema
              )
       FROM   Post p
       GROUP  BY p.thema
       ORDER  BY COUNT(p) DESC
       """)
    List<ThemeCountResponse> findTopThemes(Pageable pageable);

    default List<ThemeCountResponse> findTop3Themes() {
        return findTopThemes(PageRequest.of(0, 3));
    }

    List<Post> findTop4ByOrderByCreatedAtDesc();

    List<Post> findTop9ByOrderByViewCountDesc();
}
