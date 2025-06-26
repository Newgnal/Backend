package com.tave.alarmissue.newsroom.dto.response;

import com.tave.alarmissue.news.dto.response.NewsDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KeywordNewsResponse {
    private String keyword;
    private List<NewsDto> articles;
}