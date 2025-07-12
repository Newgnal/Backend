package com.tave.alarmissue.post.domain;

import com.tave.alarmissue.post.domain.enums.LikeType;
import com.tave.alarmissue.user.domain.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "likes")
public class PostLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long LikeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @JoinColumn
    private boolean liked;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id", nullable = true)
    private PostComment comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reply_id", nullable = true)
    private PostReply postReply;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LikeType likeType;

    @Builder
    public PostLike(UserEntity user, Post post, PostComment postComment, PostReply reply, LikeType likeType, boolean liked) {
        this.user = user;
        this.post = post;
        this.comment = postComment;
        this.postReply = reply;
        this.likeType = likeType;
        this.liked = liked;
    }
}
