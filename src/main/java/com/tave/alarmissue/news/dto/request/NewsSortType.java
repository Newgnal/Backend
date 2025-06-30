package com.tave.alarmissue.news.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;

@Getter
@RequiredArgsConstructor
public enum NewsSortType {
    LATEST("date", Sort.Direction.DESC, "최신순"),
    VIEWEST("view", Sort.Direction.DESC, "조회수순");

    private final String property;      // 정렬할 필드명
    private final Sort.Direction direction;  // 정렬 방향
    private final String description;   // 설명 (선택사항)

    // Sort 객체 생성 편의 메서드
    public Sort getSort() {
        return Sort.by(direction, property);
    }
}
