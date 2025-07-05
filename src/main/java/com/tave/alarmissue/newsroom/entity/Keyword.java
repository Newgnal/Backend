package com.tave.alarmissue.newsroom.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tave.alarmissue.global.domain.BaseTimeEntity;
import com.tave.alarmissue.user.domain.UserEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "keyword")
public class Keyword extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 10)
    @Size(min = 2, max = 10, message = "키워드는 2자 이상 10자 이하여야 합니다.")
    private String keyword;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore  // JSON 직렬화 시 무시
    private UserEntity user;

    // 순서 필드 추가
    @Column(nullable = false)
    @Builder.Default
    private Integer displayOrder = 0;
}
