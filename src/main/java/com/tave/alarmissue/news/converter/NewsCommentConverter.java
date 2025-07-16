package com.tave.alarmissue.news.converter;

import com.tave.alarmissue.news.domain.News;
import com.tave.alarmissue.news.domain.NewsComment;
import com.tave.alarmissue.news.domain.NewsVote;
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

    public static NewsCommentResponseDto toCommentResponseDto(NewsComment newsComment, NewsVoteType currentUserVoteType){
        return NewsCommentResponseDto.builder()
                .commentId(newsComment.getId())
                .comment(newsComment.getComment())
                .nickName(newsComment.getUser().getNickName())
                .createdAt(newsComment.getCreatedAt())
                .timeAgo(TimeAgoUtil.getTimeAgo(newsComment.getCreatedAt()))   //현재 시간과 계산한 값
                .voteType(currentUserVoteType)   //현재 사용자의 최신 투표 정보 사용
                .build();
    }

    public NewsComment toComment(NewsCommentRequestDto dto, UserEntity user, News news, NewsVoteType voteType) {
        return NewsComment.builder()
                .comment(dto.getComment())
                .user(user)
                .news(news)
                .voteType(voteType)
                .build();

    }



}
