package com.tave.alarmissue.post.domain.enums;

import org.springframework.data.domain.Sort;

public enum SortType {
    LATEST(Sort.by(Sort.Direction.DESC, "createdAt")),
    POPULAR(Sort.by(Sort.Direction.DESC, "viewCount"));

    private final Sort sort;

    SortType(Sort sort) {
        this.sort = sort;
    }

    public Sort getSort() {
        return sort;
    }
}
