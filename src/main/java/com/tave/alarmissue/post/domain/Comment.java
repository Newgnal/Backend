package com.tave.alarmissue.post.domain;

import com.tave.alarmissue.global.domain.BaseTimeEntity;
import com.tave.alarmissue.user.domain.UserEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.*;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String commentContent;

    @Column
    private Long likeCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Enumerated(EnumType.STRING)
    @JoinColumn(nullable = true)
    private VoteType voteTypeSnapshot;

    @Builder
    public Comment(Long commentId, String commentContent, Long likeCount, UserEntity user, Post post,VoteType voteTypeSnapshot) {
        this.commentId = commentId;
        this.commentContent = commentContent;
        this.likeCount = likeCount;
        this.user = user;
        this.post = post;
        this.voteTypeSnapshot = voteTypeSnapshot;
    }
}
