package com.tave.alarmissue.reply.domain;

import com.tave.alarmissue.comment.domain.Comment;
import com.tave.alarmissue.global.domain.BaseTimeEntity;
import com.tave.alarmissue.post.domain.Post;
import com.tave.alarmissue.user.domain.UserEntity;
import com.tave.alarmissue.vote.domain.VoteType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reply extends BaseTimeEntity {

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
    private Comment comment;

    @Enumerated(EnumType.STRING)
    @JoinColumn(nullable = true)
    private VoteType voteTypeSnapshot;

    @Builder
    public Reply (String replyContent, UserEntity user, Post post, Comment comment, VoteType voteTypeSnapshot) {
        this.replyContent = replyContent;
        this.user = user;
        this.post = post;
        this.comment = comment;
        this.voteTypeSnapshot = voteTypeSnapshot;
    }


}
