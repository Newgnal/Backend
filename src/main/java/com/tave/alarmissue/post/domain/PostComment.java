package com.tave.alarmissue.post.domain;

import com.tave.alarmissue.global.domain.BaseTimeEntity;
import com.tave.alarmissue.post.domain.enums.VoteType;
import com.tave.alarmissue.user.domain.UserEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostComment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String comment;

    @Column
    private Long likeCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @OneToMany(mappedBy = "postComment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostReply> replies = new ArrayList<>();

    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostLike> likes = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostReport> reports = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @JoinColumn(nullable = true)
    private VoteType voteType;



    @Builder
    public PostComment(Long commentId, String comment, Long likeCount, UserEntity user, Post post, VoteType voteType) {
        this.commentId = commentId;
        this.comment = comment;
        this.likeCount = likeCount;
        this.user = user;
        this.post = post;
        this.voteType = voteType;
    }
}
