package com.tave.alarmissue.post.repository;

import com.tave.alarmissue.news.domain.enums.Thema;

public interface ThemeCountProjection {
    Thema getThema(); // enum 값
    Long  getCnt();   // 게시글 수
}
