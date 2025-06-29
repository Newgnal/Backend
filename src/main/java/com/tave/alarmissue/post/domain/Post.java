package com.tave.alarmissue.post.domain;

import com.tave.alarmissue.global.domain.BaseTimeEntity;
import com.tave.alarmissue.news.enums.Thema;
import com.tave.alarmissue.user.domain.UserEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;


    @Column(nullable = false, length = 100)
    private String postTitle;
    @Column(columnDefinition = "TEXT", nullable = false)
    private String postContent;

    @Column
    private String articleUrl; //기사 url

    @Column
    private Thema thema;

    @Column
    private Boolean hasVote =false ;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;


    @Builder
    public Post(String postTitle, String postContent, String articleUrl, Thema thema, UserEntity user, boolean hasVote) {
        this.postTitle = postTitle;
        this.postContent = postContent;
        this.articleUrl = articleUrl;
        this.thema = thema;
        this.user = user;
        this.hasVote = hasVote;
    }

    public void Update(String postTitle,String postContent, String articleUrl,Thema thema, boolean hasVote)
    {
        this.postTitle=postTitle;
        this.postContent=postContent;
        this.articleUrl=articleUrl;
        this.thema = thema;
        this.hasVote=hasVote;
    }
}
