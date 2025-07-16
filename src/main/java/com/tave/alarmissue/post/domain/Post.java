package com.tave.alarmissue.post.domain;

import com.tave.alarmissue.global.domain.BaseTimeEntity;
import com.tave.alarmissue.news.domain.enums.Thema;
import com.tave.alarmissue.user.domain.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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
    private Long likeCount;


    @Column
    private String articleUrl; //기사 url

    @Column
    private Thema thema;

    @Column
    private Boolean hasVote = false ;

    @Column
    private Long viewCount ;

    @Column
    private Long commentCount ;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostComment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostLike> likes = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostReply> replies = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostReport> reports = new ArrayList<>();

    @Builder
    public Post(String postTitle, String postContent, Long likeCount, String articleUrl, Thema thema, UserEntity user, boolean hasVote, Long viewCount,Long commentCount) {
        this.postTitle = postTitle;
        this.postContent = postContent;
        this.likeCount = likeCount;
        this.articleUrl = articleUrl;
        this.thema = thema;
        this.user = user;
        this.hasVote = hasVote;
        this.viewCount = viewCount;
        this.commentCount = commentCount;
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
