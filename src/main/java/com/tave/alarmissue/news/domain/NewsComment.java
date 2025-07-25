package com.tave.alarmissue.news.domain;

import com.tave.alarmissue.global.domain.BaseTimeEntity;
import com.tave.alarmissue.news.domain.enums.NewsVoteType;
import com.tave.alarmissue.report.domain.Report;
import com.tave.alarmissue.post.domain.PostLike;
import com.tave.alarmissue.user.domain.UserEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="news_comment")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class NewsComment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="news_id",nullable = false)
    private News news;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String comment;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private NewsVoteType voteType;

    // 답글 관련 필드 추가
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="parent_id")
    private NewsComment parentComment;       // 부모 댓글

    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL, orphanRemoval = true) //답글들도 함께 삭제
    private List<NewsComment> replies = new ArrayList<>();  // 답글 리스트

    @OneToMany(mappedBy = "newsComment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Report> reports = new ArrayList<>();

    public void updateContent(String newComment) {
        this.comment=newComment;
    }

    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NewsCommentLike> likes = new ArrayList<>();

    // 좋아요 개수 필드 추가 (성능 최적화용)
    @Column(name = "like_count", nullable = false)
    private Long likeCount = 0L;

    // 좋아요 관련 메서드
    public void incrementLikeCount() {
        this.likeCount++;
    }

    public void updateVoteType(NewsVoteType voteType){
        this.voteType=voteType;
    }

    public void decrementLikeCount() {
        if (this.likeCount > 0) {
            this.likeCount--;
        }
    }

}
