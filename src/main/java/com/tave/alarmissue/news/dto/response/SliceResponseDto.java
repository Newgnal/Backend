package com.tave.alarmissue.news.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class SliceResponseDto<T> {
    private List<T> content;    //실제 데이터. NewsResponseDto 객체들의 List
    private boolean hasNext;   //다음 페이지 존재 여부
    private int currentPage;   //현재 페이지 번호
}
