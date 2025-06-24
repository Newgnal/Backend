package com.tave.alarmissue.news.domain;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity   //JPA가 관리
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="news")
public class News {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column
    private String title;
    @Column
    private String url;
    @Column
    private String content;
//    @Column
//    private String source;   //
    @Column
    private String thema;    //"반도체", "IT" 등등
    @Column
    private LocalDateTime date;    //기사 날짜

    @Column
    private double sentiment;


}
