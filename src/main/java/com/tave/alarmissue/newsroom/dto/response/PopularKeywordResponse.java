package com.tave.alarmissue.newsroom.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PopularKeywordResponse {
    private String keyword;
    private Long count;
}
