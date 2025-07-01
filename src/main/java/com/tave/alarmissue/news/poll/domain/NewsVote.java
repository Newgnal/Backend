package com.tave.alarmissue.news.poll.domain;

import com.tave.alarmissue.news.domain.News;
import com.tave.alarmissue.user.domain.UserEntity;
import com.tave.alarmissue.vote.domain.VoteType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="news_vote")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class NewsVote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)   //한 뉴스에 하나의 튜표
    @JoinColumn(name="news_id")
    private News news;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NewsVoteType voteType;

    @Column(nullable = false)
    private String question;    //"이 뉴스가 [반도체/AI]에 어떤 영향을 줄까요?"

}
