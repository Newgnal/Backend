package com.tave.alarmissue.newsroom.dto.response;

import com.tave.alarmissue.news.dto.response.RepresentativeNewsDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PopularKeywordResponse {
    private String keyword;
    private Long count;
    private RepresentativeNewsDto representativeNews;
}
