package com.tave.alarmissue.news.service;

import com.tave.alarmissue.news.converter.NewsCommentConverter;
import com.tave.alarmissue.news.domain.News;
import com.tave.alarmissue.news.domain.NewsComment;
import com.tave.alarmissue.news.domain.NewsVote;
import com.tave.alarmissue.news.domain.enums.NewsVoteType;
import com.tave.alarmissue.news.dto.request.NewsCommentRequestDto;
import com.tave.alarmissue.news.dto.response.NewsCommentListResponseDto;
import com.tave.alarmissue.news.dto.response.NewsCommentResponseDto;
import com.tave.alarmissue.news.exceptions.NewsException;
import com.tave.alarmissue.news.repository.NewsCommentRepository;
import com.tave.alarmissue.news.repository.NewsRepository;
import com.tave.alarmissue.news.repository.NewsVoteRepository;
import com.tave.alarmissue.user.domain.UserEntity;
import com.tave.alarmissue.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.tave.alarmissue.news.exceptions.NewsErrorCode.*;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NewsCommentService {

    private final NewsCommentRepository newsCommentRepository;
    private final UserRepository userRepository;
    private final NewsRepository newsRepository;
    private final NewsVoteRepository newsVoteRepository;
    private final NewsCommentConverter newsCommentConverter;

    @Transactional
    public NewsCommentResponseDto createComment(NewsCommentRequestDto dto, Long userId, Long newsId){

        UserEntity user=userRepository.findById(userId).orElseThrow(()->new NewsException(USER_ID_NOT_FOUND,"사용자가 없습니다."));
        News news=newsRepository.findById(newsId).orElseThrow(()->new NewsException(NEWS_ID_NOT_FOUND,"newsId:"+newsId));


        NewsVoteType newsVoteType = newsVoteRepository.findVoteTypeByNewsIdAndUserId(newsId,userId).orElse(null);
        NewsComment newsComment=newsCommentConverter.toComment(dto,user,news,newsVoteType);
        NewsComment saved=newsCommentRepository.save(newsComment);
        news.incrementCommentCount();

        return NewsCommentConverter.toCommentResponseDto(saved,newsVoteType);
    }


    public NewsCommentListResponseDto getCommentsByNewsId(Long newsId) {

        News news=newsRepository.findById(newsId).orElseThrow(()->new NewsException(NEWS_ID_NOT_FOUND,"newsId:"+newsId));
        // 1. 댓글 조회
        List<NewsComment> comments = newsCommentRepository.findByNewsIdOrderByCreatedAtDesc(newsId);

        // 2. 댓글 작성자 ID 추출
        List<Long> userIds = comments.stream()
                .map(comment -> comment.getUser().getId())
                .distinct()
                .toList();

        // 3. 작성자들의 voteType 한 번에 조회
        Map<Long, NewsVoteType> voteTypeMap = newsVoteRepository.findVoteTypesByNewsIdAndUserIds(newsId, userIds)
                .stream()
                .collect(Collectors.toMap(
                        vote -> vote.getUser().getId(),
                        NewsVote::getVoteType
                ));

        // 4. DTO 변환
        List<NewsCommentResponseDto> commentDtos = comments.stream()
                .map(comment -> {
                    NewsVoteType voteType = voteTypeMap.get(comment.getUser().getId());
                    return NewsCommentConverter.toCommentResponseDto(comment, voteType);
                })
                .toList();

        // 5. 전체 응답 DTO 생성
        Long totalCount = (long) comments.size();
        return new NewsCommentListResponseDto(newsId, totalCount, commentDtos);
    }


    @Transactional
    public void deleteComment(Long commentId, Long userId) {
        // 해당 뉴스의 해당 사용자가 작성한 댓글만 조회
        NewsComment comment = newsCommentRepository.findByIdAndUserId(commentId, userId)
                .orElseThrow(() -> new NewsException(COMMENT_ID_NOT_FOUND,
                        "해당 뉴스에서 본인이 작성한 댓글을 찾을 수 없습니다."));

        News news = comment.getNews();

        // 댓글 삭제
        newsCommentRepository.delete(comment);

        // 댓글 개수 감소
        news.decrementCommentCount();
    }

    @Transactional
    public NewsCommentResponseDto updateComment(Long commentId, Long userId, NewsCommentRequestDto dto) {
        NewsComment comment = newsCommentRepository.findByIdAndNewsIdAndUserId(commentId, dto.getNewsId(), userId)
                .orElseThrow(() -> new NewsException(COMMENT_ID_NOT_FOUND,
                        "해당 뉴스에서 본인이 작성한 댓글을 찾을 수 없습니다."));

        //댓글 내용 업데이트
        comment.updateContent(dto.getComment().trim());
        NewsVoteType currentUserVoteType = newsVoteRepository
                .findVoteTypeByNewsIdAndUserId(dto.getNewsId(), userId)
                .orElse(null);

        return NewsCommentConverter.toCommentResponseDto(comment,currentUserVoteType);

    }
}
