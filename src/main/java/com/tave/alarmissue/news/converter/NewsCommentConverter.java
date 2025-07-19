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

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class NewsCommentConverter {
    public static NewsCommentResponseDto toCommentResponseDto(NewsComment newsComment, NewsVoteType voteType) {
        return NewsCommentResponseDto.builder()
                .commentId(newsComment.getId())
                .comment(newsComment.getComment())
                .nickName(newsComment.getUser().getNickName())
                .createdAt(newsComment.getCreatedAt())
                .timeAgo(TimeAgoUtil.getTimeAgo(newsComment.getCreatedAt()))   //현재 시간과 계산한 값
                .voteType(voteType)
                .parentId(newsComment.getParentComment() != null ? newsComment.getParentComment().getId() : null)
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

//    public static NewsCommentListResponseDto toCommentListResponseDto(
//            Long totalCount,
//            List<NewsComment> comments,
//            NewsVoteType voteType) {
//
//        List<NewsCommentResponseDto> commentResponseDtos = comments.stream()
//                .map(comment -> toCommentWithRepliesDto(comment, voteType)) // voteType 추가
//                .collect(Collectors.toList());
//
//        return NewsCommentListResponseDto.builder()
//                .totalCount(totalCount)
//                .comments(commentResponseDtos)
//                .build();
//    }

    // 답글 포함 댓글 변환
    public static NewsCommentResponseDto toCommentWithRepliesDto(NewsComment newsComment, Long userId, Long newsId, NewsVoteType voteType) {
        {
            // 답글들을 DTO로 변환
            List<NewsCommentResponseDto> replyDtos = newsComment.getReplies().stream()
                    .sorted(Comparator.comparing(NewsComment::getCreatedAt)) // 오래된 순
                    .map(reply -> toCommentResponseDto(reply, voteType)) // voteType 함께 넘김
                    .collect(Collectors.toList());

            return NewsCommentResponseDto.builder()
                    .commentId(newsComment.getId())
                    .comment(newsComment.getComment())
                    .nickName(newsComment.getUser().getNickName())
                    .createdAt(newsComment.getCreatedAt())
                    .timeAgo(TimeAgoUtil.getTimeAgo(newsComment.getCreatedAt()))
                    .voteType(newsComment.getVoteType())
                    .parentId(null) // 원댓글이므로 null
                    .replies(replyDtos) // 답글들 포함
                    .replyCount(replyDtos.size())
                    .build();
        }

    }
}
