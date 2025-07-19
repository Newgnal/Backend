package com.tave.alarmissue.news.dto.request.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;

@Getter
@RequiredArgsConstructor
public enum NewsSortType {
    LATEST,
    POPULAR

}
