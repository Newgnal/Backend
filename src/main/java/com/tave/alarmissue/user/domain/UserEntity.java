package com.tave.alarmissue.user.domain;

import com.tave.alarmissue.global.domain.BaseTimeEntityWithDeletion;
import com.tave.alarmissue.newsroom.entity.Keyword;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Builder
@AllArgsConstructor
@Table(name = "`user`")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE `user` SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class UserEntity extends BaseTimeEntityWithDeletion {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(length = 30, nullable = false, unique = false)
    private String email;

    // 닉네임
    @Column(length = 20, nullable = true, unique = true)
    private String nickName;

    @Column(length = 1024)
    private String imageUrl;

    private boolean enabled;

    @Enumerated(STRING)
    private Role role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Keyword> keywords = new ArrayList<>();

    public void changeNickname(String nickname) {
        this.nickName = nickname;
    }
}
