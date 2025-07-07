package com.tave.alarmissue.news.converter;

import com.tave.alarmissue.news.domain.News;
import com.tave.alarmissue.news.domain.NewsComment;
import com.tave.alarmissue.news.domain.enums.NewsVoteType;
import com.tave.alarmissue.news.dto.request.NewsCommentRequestDto;
import com.tave.alarmissue.news.dto.response.NewsCommentResponseDto;
import com.tave.alarmissue.user.domain.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class NewsCommentConverter {
    public static NewsCommentResponseDto toCommentResponseDto(NewsComment newsComment){
        return NewsCommentResponseDto.builder()
                .newsId(newsComment.getId())
                .commentContent(newsComment.getCommentContent())
                .nickName(newsComment.getUser().getNickName())
                .createdAt(newsComment.getCreatedAt())
                .updatedAt(newsComment.getUpdatedAt())
                .voteType(newsComment.getVoteType() != null ? newsComment.getVoteType() : null)
                .build();
    }

    public NewsComment toComment(NewsCommentRequestDto dto, UserEntity user, News news, NewsVoteType voteType) {
        return NewsComment.builder()
                .commentContent(dto.getCommentContent())
                .user(user)
                .news(news)
                .voteType(voteType)
                .build();

    }
}
