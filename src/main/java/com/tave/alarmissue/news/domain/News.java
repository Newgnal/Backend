package com.tave.alarmissue.news.domain;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tave.alarmissue.global.domain.BaseTimeEntityWithDeletion;
import com.tave.alarmissue.news.domain.enums.Thema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@NoArgsConstructor(access = PROTECTED)
@Getter
@AllArgsConstructor
@Table(name="news")
@SQLDelete(sql = "UPDATE news SET deleted_at = NOW() WHERE id = ?")
@Where(clause = "deleted_at IS NULL")
public class News extends BaseTimeEntityWithDeletion {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false, length = 500)
    private String title;

    @Column(nullable = false, unique = true, length = 1000)
    private String url;

    @Column(nullable = true, unique = true, length=1000)
    private String imageUrl;

    @Column(columnDefinition = "TEXT")
    private String imageCaption;

    @Column(columnDefinition = "LONGTEXT")
    private String contentUrl;

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

    @Column
    private String summary;


    @OneToMany(mappedBy = "news", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NewsComment> comments = new ArrayList<>();

    @Builder
    public News(String title,
                String url,
                String imageUrl,
                String contentUrl,
                String imageCaption,
                String content,
                String source,
                Thema thema,
                LocalDateTime date,
                Double sentiment,
                Long view,
                Long commentNum,
                Long voteNum,
                String summary) {
        this.title = title;
        this.url = url;
        this.imageUrl = imageUrl;
        this.contentUrl = contentUrl;
        this.imageCaption = imageCaption;
        this.source = source;
        this.thema = thema;
        this.date = date;
        this.sentiment = sentiment;
        this.view = view;
        this.commentNum = commentNum;
        this.voteNum = voteNum;
        this.summary = summary;
    }

    public void incrementView(){
        this.view+=1;
    }

    public void incrementCommentCount(){
        this.commentNum+=1;
    }

    public void decrementCommentCount(){
        if(this.commentNum>0) {
            this.commentNum-=1;
        }
    }
    public void incrementVoteCount(){
        this.voteNum+=1;
    }
}
