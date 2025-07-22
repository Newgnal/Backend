package com.tave.alarmissue.news.domain;

import com.tave.alarmissue.user.domain.UserEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Builder
@Table(name="news_comment_like")
public class NewsCommentLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="comment_id", nullable = false)
    private NewsComment comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", nullable = false)
    private UserEntity user;

    public NewsCommentLike(NewsComment comment, UserEntity user) {
        this.comment = comment;
        this.user = user;
    }





}
