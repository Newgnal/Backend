package com.tave.alarmissue.report.domain;

import com.tave.alarmissue.news.domain.NewsComment;
import com.tave.alarmissue.post.domain.Post;
import com.tave.alarmissue.post.domain.PostComment;
import com.tave.alarmissue.post.domain.PostReply;
import com.tave.alarmissue.post.domain.enums.TargetType;
import com.tave.alarmissue.user.domain.UserEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "Report")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportId;

    @JoinColumn
    private boolean reported=false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = true)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id", nullable = true)
    private PostComment postComment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reply_id", nullable = true)
    private PostReply postReply;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "news_comment_id", nullable = true)
    private NewsComment newsComment;

    @Enumerated(EnumType.STRING)
    @JoinColumn(nullable = false)
    private TargetType targetType;

    @Builder
    public Report(boolean reported, UserEntity user, Post post, PostComment postComment, PostReply postReply,NewsComment newsComment, TargetType targetType) {
        this.reported = reported;
        this.user = user;
        this.post = post;
        this.postComment = postComment;
        this.postReply = postReply;
        this.newsComment = newsComment;
        this.targetType = targetType;
    }
}
