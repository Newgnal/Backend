package com.tave.alarmissue.news.repository;

import com.tave.alarmissue.news.domain.enums.Thema;

import java.time.LocalDateTime;

public interface NewsMapping {

    Long getId();
    String getTitle();
    String getSource();
    LocalDateTime getDate();
    Thema getThema();
    double getSentiment();
}
