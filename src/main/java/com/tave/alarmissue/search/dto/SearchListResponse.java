package com.tave.alarmissue.search.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class SearchListResponse {
    private Long id;
    private String content;
}
