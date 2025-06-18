package com.tave.alarmissue.user.domain;

import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Builder
@AllArgsConstructor
@Table(name = "`user`")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserEntity {
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

}
