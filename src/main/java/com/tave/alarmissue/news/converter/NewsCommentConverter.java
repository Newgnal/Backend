package com.tave.alarmissue.news.converter;

import com.tave.alarmissue.news.domain.News;
import com.tave.alarmissue.news.domain.NewsComment;
import com.tave.alarmissue.news.domain.enums.NewsVoteType;
import com.tave.alarmissue.news.dto.request.NewsCommentCreateRequestDto;
import com.tave.alarmissue.news.dto.response.NewsCommentResponseDto;
import com.tave.alarmissue.news.util.TimeAgoUtil;
import com.tave.alarmissue.user.domain.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class NewsCommentConverter {
    public static NewsCommentResponseDto toCommentResponseDto(NewsComment newsComment){
        return NewsCommentResponseDto.builder()
                .newsId(newsComment.getNews().getId())
                .commentId(newsComment.getId())
                .comment(newsComment.getComment())
                .nickName(newsComment.getUser().getNickName())
                .createdAt(newsComment.getCreatedAt())
                .timeAgo(TimeAgoUtil.getTimeAgo(newsComment.getCreatedAt()))   //현재 시간과 계산한 값
                .voteType(newsComment.getVoteType() != null ? newsComment.getVoteType() : null)
                .build();
    }

    public NewsComment toComment(NewsCommentCreateRequestDto dto, UserEntity user, News news, NewsVoteType voteType) {
        return NewsComment.builder()
                .comment(dto.getComment())
                .user(user)
                .news(news)
                .voteType(voteType)
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
