package com.tave.alarmissue.global.utils;

import com.tave.alarmissue.global.dto.response.PagedResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PagenationUtils {

    /**
     * 페이지네이션 응답 생성
     */
    public static <T> PagedResponse<T> createPagedResponse(List<T> content,
                                                           boolean hasNext,
                                                           Long nextLastId,
                                                           Long totalCount) {
        return PagedResponse.<T>builder()
                .content(content)
                .hasNext(hasNext)
                .nextLastId(nextLastId)
                .size(content.size())
                .totalCount(totalCount)
                .build();
    }

    /**
     * 페이지 크기 검증 및 조정
     */
    public static int validateAndAdjustPageSize(Integer size) {
        if (size == null) {
            return 10; // 기본값
        }
        return Math.min(Math.max(size, 1), 50); // 1~50 사이로 제한
    }

    /**
     * 다음 페이지 존재 여부 및 실제 데이터 추출
     */
    public static <T> PaginationResult<T> processPaginationData(List<T> dataList, int requestedSize) {
        boolean hasNext = dataList.size() > requestedSize;
        List<T> actualData = hasNext ? dataList.subList(0, requestedSize) : dataList;

        return new PaginationResult<>(actualData, hasNext);
    }

    @Data
    @AllArgsConstructor
    public static class PaginationResult<T> {
        private List<T> data;
        private boolean hasNext;
    }
}
