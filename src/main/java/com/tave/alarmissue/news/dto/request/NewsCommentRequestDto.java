package com.tave.alarmissue.news.dto.request;


import com.tave.alarmissue.news.domain.enums.NewsVoteType;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
//댓글 생성 요청 DTO
public class NewsCommentRequestDto {
    private String commentContent;
}
