package com.tave.alarmissue.post.domain;

import com.tave.alarmissue.post.domain.enums.ReportType;
import com.tave.alarmissue.user.domain.UserEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class PostReport {

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


    @JoinColumn(nullable = false)
    private ReportType reportType;

    @Builder
    public PostReport(boolean reported, UserEntity user, Post post, PostComment postComment,PostReply postReply, ReportType reportType) {
        this.reported = reported;
        this.user = user;
        this.post = post;
        this.postComment = postComment;
        this.postReply = postReply;
        this.reportType = reportType;
    }
}
