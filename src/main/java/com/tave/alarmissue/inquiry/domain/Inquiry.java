package com.tave.alarmissue.inquiry.domain;

import com.tave.alarmissue.inquiry.dto.request.InquiryRequest;
import com.tave.alarmissue.user.domain.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@NoArgsConstructor(access = PROTECTED)
@Getter
@AllArgsConstructor
public class Inquiry {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String email;

    private String title;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Builder
    public Inquiry(String email, String title, String content, UserEntity user) {
        this.email = email;
        this.title = title;
        this.content = content;
        this.user = user;
    }

    // 정적 생성 메서드 추가
    public static Inquiry createFrom(InquiryRequest request, UserEntity user) {
        return Inquiry.builder()
                .email(request.getEmail())
                .title(request.getTitle())
                .content(request.getBody())
                .user(user)
                .build();
    }
}
