package com.tave.alarmissue.post.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class PageResponse<T> {
    private List<T> content;      // 실제 데이터 리스트
    private int pageNumber;       // 현재 페이지 번호
    private int pageSize;         // 페이지 크기
    private boolean last;         // 마지막 페이지 여부

}