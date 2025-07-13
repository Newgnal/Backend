package com.tave.alarmissue.post.domain;

import com.tave.alarmissue.post.domain.enums.VoteType;
import com.tave.alarmissue.user.domain.UserEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@Builder
public class PostVote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VoteType voteType;

    @Builder
    public PostVote(Long id, UserEntity user, Post post, VoteType voteType) {
        this.id = id;
        this.user = user;
        this.post = post;
        this.voteType = voteType;
    }

    public void updateVoteType(VoteType voteType) {
        this.voteType = voteType;
    }
}
