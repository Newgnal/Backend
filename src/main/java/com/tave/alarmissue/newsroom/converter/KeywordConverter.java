package com.tave.alarmissue.newsroom.converter;

import com.tave.alarmissue.newsroom.dto.response.KeywordResponse;
import com.tave.alarmissue.newsroom.entity.Keyword;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public final class KeywordConverter {

    /**
     * Keyword 엔티티를 KeywordResponse DTO로 변환
     */
    public static KeywordResponse toResponse(Keyword keyword) {
        if (keyword == null) {
            return null;
        }

        return KeywordResponse.builder()
                .id(keyword.getId())
                .keyword(keyword.getKeyword())
                .createdAt(keyword.getCreatedAt())
                .userId(keyword.getUser() != null ? keyword.getUser().getId() : null)
                .build();
    }

    /**
     * Keyword 엔티티 리스트를 KeywordResponse DTO 리스트로 변환
     */
    public static List<KeywordResponse> toResponseList(List<Keyword> keywords) {
        if (keywords == null || keywords.isEmpty()) {
            return new ArrayList<>();
        }

        return keywords.stream()
                .map(KeywordConverter::toResponse)
                .collect(Collectors.toList());
    }
}

