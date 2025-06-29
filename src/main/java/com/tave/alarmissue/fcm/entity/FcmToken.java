package com.tave.alarmissue.fcm.entity;

import com.tave.alarmissue.global.domain.BaseTimeEntity;
import com.tave.alarmissue.user.domain.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@Table(name = "fcm_token")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FcmToken extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(nullable = false, unique = true, length = 255)
    private String fcmToken;

    //---- method ---
    public void updateUser(UserEntity user) {
        this.user = user;
    }

    public void updateFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

}