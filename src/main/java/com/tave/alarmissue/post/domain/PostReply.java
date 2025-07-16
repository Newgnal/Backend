package com.tave.alarmissue.post.domain;

import com.tave.alarmissue.global.domain.BaseTimeEntity;
import com.tave.alarmissue.user.domain.UserEntity;
import com.tave.alarmissue.post.domain.enums.VoteType;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostReply extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long replyId;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String replyContent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id", nullable = false)
    private PostComment postComment;

    @OneToMany(mappedBy = "postReply", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostLike> likes = new ArrayList<>();


    @OneToMany(mappedBy = "postReply", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostReport> reports = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @JoinColumn(nullable = true)
    private VoteType voteType;

    @Column
    private Long likeCount;

    @Builder
    public PostReply (String replyContent, UserEntity user, Post post, PostComment postComment, VoteType voteType, Long likeCount) {
        this.replyContent = replyContent;
        this.user = user;
        this.post = post;
        this.postComment = postComment;
        this.voteType = voteType;
        this.likeCount = likeCount;
    }


}
