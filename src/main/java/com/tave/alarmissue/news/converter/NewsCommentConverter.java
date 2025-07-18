package com.tave.alarmissue.news.converter;

import com.tave.alarmissue.news.domain.News;
import com.tave.alarmissue.news.domain.NewsComment;
import com.tave.alarmissue.news.domain.enums.NewsVoteType;
import com.tave.alarmissue.news.dto.request.NewsCommentRequestDto;
import com.tave.alarmissue.news.dto.response.NewsCommentListResponseDto;
import com.tave.alarmissue.news.dto.response.NewsCommentResponseDto;
import com.tave.alarmissue.news.util.TimeAgoUtil;
import com.tave.alarmissue.user.domain.UserEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class NewsCommentConverter {
    public static NewsCommentResponseDto toCommentResponseDto(NewsComment newsComment){
        return NewsCommentResponseDto.builder()
                .commentId(newsComment.getId())
                .comment(newsComment.getComment())
                .likeCount(0L)
                .nickName(newsComment.getUser().getNickName())
                .createdAt(newsComment.getCreatedAt())
                .timeAgo(TimeAgoUtil.getTimeAgo(newsComment.getCreatedAt()))   //현재 시간과 계산한 값
                .voteType(newsComment.getVoteType() != null ? newsComment.getVoteType() : null)
                .build();
    }

    public NewsComment toComment(NewsCommentRequestDto dto, UserEntity user, News news, NewsVoteType voteType) {
        return NewsComment.builder()
                .comment(dto.getComment())
                .user(user)
                .news(news)
                .likeCount(0L)
                .voteType(voteType)
                .build();

    }

    //댓글 목록 변환
    public static NewsCommentListResponseDto toCommentListResponseDto(Long newsId, Long totalCount, List<NewsComment> comments) {
        List<NewsCommentResponseDto> commentResponseDtos = comments.stream()
                .map(NewsCommentConverter::toCommentResponseDto)
                .collect(Collectors.toList());

        return NewsCommentListResponseDto.builder()
                .newsId(newsId)
                .totalCount(totalCount)
                .comments(commentResponseDtos)
                .build();
    }
}
