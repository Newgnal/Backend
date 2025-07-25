package com.tave.alarmissue.news.converter;

import com.tave.alarmissue.news.domain.News;
import com.tave.alarmissue.news.domain.NewsComment;
import com.tave.alarmissue.news.domain.NewsVote;
import com.tave.alarmissue.news.domain.enums.NewsVoteType;
import com.tave.alarmissue.news.dto.request.NewsCommentRequestDto;
import com.tave.alarmissue.news.dto.request.NewsReplyRequest;
import com.tave.alarmissue.news.dto.response.NewsCommentListResponseDto;
import com.tave.alarmissue.news.dto.response.NewsCommentResponseDto;
import com.tave.alarmissue.news.dto.response.NewsVoteResponseDto;
import com.tave.alarmissue.news.repository.NewsVoteRepository;
import com.tave.alarmissue.news.util.TimeAgoUtil;
import com.tave.alarmissue.user.domain.UserEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class NewsCommentConverter {
    //답글용
    public static NewsCommentResponseDto toCommentResponseDto(NewsComment newsComment, NewsVoteType currentUserVoteType){
        return NewsCommentResponseDto.builder()
                .commentId(newsComment.getId())
                .comment(newsComment.getComment())
                .likeCount(newsComment.getLikeCount())
                .nickName(newsComment.getUser().getNickName())
                .createdAt(newsComment.getCreatedAt())
                .timeAgo(TimeAgoUtil.getTimeAgo(newsComment.getCreatedAt()))   //현재 시간과 계산한 값
                .parentId(newsComment.getParentComment()!=null ? newsComment.getParentComment().getId():null)
                .voteType(currentUserVoteType)
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

    public static NewsCommentListResponseDto toCommentListResponseDto(Long newsId, Long totalCount, List<NewsComment> comments,NewsVoteType currentUserVoteType) {
        List<NewsCommentResponseDto> commentResponseDtos = comments.stream()
                .map(comment->toCommentWithRepliesDto(comment,currentUserVoteType))
                .collect(Collectors.toList());

        return NewsCommentListResponseDto.builder()
                .newsId(newsId)
                .totalCount(totalCount)
                .comments(commentResponseDtos)
                .build();
    }

    // 답글 포함 댓글 변환
    public static NewsCommentResponseDto toCommentWithRepliesDto(NewsComment newsComment,NewsVoteType currentUserVoteType) {
        // 답글들을 DTO로 변환
        List<NewsCommentResponseDto> replyDtos = newsComment.getReplies().stream()
                .sorted((r1, r2) -> r1.getCreatedAt().compareTo(r2.getCreatedAt())) // 답글은 오래된 순
                .map(reply->toCommentResponseDto(reply,currentUserVoteType))
                .collect(Collectors.toList());

        return NewsCommentResponseDto.builder()
                .commentId(newsComment.getId())
                .comment(newsComment.getComment())
                .nickName(newsComment.getUser().getNickName())
                .createdAt(newsComment.getCreatedAt())
                .timeAgo(TimeAgoUtil.getTimeAgo(newsComment.getCreatedAt()))
                .voteType(newsComment.getVoteType())
                .likeCount(newsComment.getLikeCount())
                .voteType(currentUserVoteType)
                .parentId(null) // 원댓글이므로 null
                .replies(replyDtos) // 답글들 포함
                .replyCount(replyDtos.size())
                .build();
    }

}