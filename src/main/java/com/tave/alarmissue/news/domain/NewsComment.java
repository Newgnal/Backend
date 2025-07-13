package com.tave.alarmissue.news.domain;

import com.tave.alarmissue.global.domain.BaseTimeEntity;
import com.tave.alarmissue.news.domain.enums.NewsVoteType;
import com.tave.alarmissue.user.domain.UserEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    public void updateContent(String newComment) {
        this.comment=newComment;

    }

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name="parent_id")
//    private Comment parent;       //답글을 위한 부모 댓글

}
