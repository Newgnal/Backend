package com.tave.alarmissue.post.domain;

import com.tave.alarmissue.user.domain.UserEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;


    @Column(nullable = false, length = 100)
    private String postTitle;
    @Column(columnDefinition = "TEXT", nullable = false)
    private String postContent;

    private String postImage;

    @Enumerated(EnumType.STRING)

    private PostType postType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;
}
