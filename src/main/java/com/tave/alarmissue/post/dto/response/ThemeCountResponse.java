package com.tave.alarmissue.post.dto.response;

import com.tave.alarmissue.news.domain.enums.Thema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ThemeCountResponse {
    private final Thema thema;
}
