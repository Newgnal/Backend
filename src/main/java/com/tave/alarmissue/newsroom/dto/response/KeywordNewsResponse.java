package com.tave.alarmissue.newsroom.dto.response;

import com.tave.alarmissue.global.dto.response.PagedResponse;
import com.tave.alarmissue.news.dto.response.NewsDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KeywordNewsResponse {
    private String keyword;
    private PagedResponse<NewsDto> newsData;
}