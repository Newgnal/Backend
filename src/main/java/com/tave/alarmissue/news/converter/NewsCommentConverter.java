package com.tave.alarmissue.news.converter;

import com.tave.alarmissue.news.domain.News;
import com.tave.alarmissue.news.domain.NewsComment;
import com.tave.alarmissue.news.domain.enums.NewsVoteType;
import com.tave.alarmissue.news.dto.request.NewsCommentCreateRequestDto;
import com.tave.alarmissue.news.dto.response.NewsCommentCountResponseDto;
import com.tave.alarmissue.news.dto.response.NewsCommentCreateResponseDto;
import com.tave.alarmissue.news.dto.response.NewsCommentResponseDto;
import com.tave.alarmissue.user.domain.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class NewsCommentConverter {
    public static NewsCommentResponseDto toCommentResponseDto(NewsComment newsComment){
        return NewsCommentResponseDto.builder()
                .newsId(newsComment.getNews().getId())
                .commentContent(newsComment.getCommentContent())
                .nickName(newsComment.getUser().getNickName())
                .createdAt(newsComment.getCreatedAt())
                .updatedAt(newsComment.getUpdatedAt())
                .voteType(newsComment.getVoteType() != null ? newsComment.getVoteType() : null)
                .build();
    }

    public NewsComment toComment(NewsCommentCreateRequestDto dto, UserEntity user, News news, NewsVoteType voteType) {
        return NewsComment.builder()
                .commentContent(dto.getCommentContent())
                .user(user)
                .news(news)
                .voteType(voteType)
                .build();

    }

    // 댓글 생성 응답 DTO 변환 메서드 추가
    public static NewsCommentCreateResponseDto toCommentCreateResponseDto(NewsComment newsComment, Long totalCommentCount) {
        NewsCommentResponseDto commentResponse = toCommentResponseDto(newsComment);

        return NewsCommentCreateResponseDto.builder()
                .comment(commentResponse)
                .totalCommentCount(totalCommentCount)
                .build();
    }

    // 댓글 개수 응답 DTO 변환 메서드 추가
//    public static NewsCommentCountResponseDto toCommentCountResponseDto(Long newsId, Long count) {
//        return NewsCommentCountResponseDto.builder()
//                .newsId(newsId)
//                .count(count)
//                .build();
//    }
}
