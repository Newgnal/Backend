package com.tave.alarmissue.news.domain;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tave.alarmissue.news.domain.enums.Thema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@NoArgsConstructor(access = PROTECTED)
@Getter
@AllArgsConstructor
@Table(name="news")
public class News {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false, length = 500)
    private String title;

    @Column(nullable = false, unique = true, length = 1000)
    private String url;

    @Column(nullable = false, unique = true, length=1000)
    private String imageUrl;

    @Column
    private String content;

    @Column(nullable = false, length = 100)
    private String source; //ex) 매일경제

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Thema thema;

    @Column
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd")
    private LocalDateTime date;    //기사 날짜

    @Column
    private Double sentiment;

    @Column
    private Long view;

    @Column
    private Long commentNum;

    @Column
    private Long voteNum;


}