package com.tave.alarmissue.news.service;

import com.tave.alarmissue.news.converter.NewsCommentConverter;
import com.tave.alarmissue.news.domain.News;
import com.tave.alarmissue.news.domain.NewsComment;
import com.tave.alarmissue.news.domain.enums.NewsVoteType;
import com.tave.alarmissue.news.dto.request.NewsCommentRequestDto;
import com.tave.alarmissue.news.dto.response.NewsCommentListResponseDto;
import com.tave.alarmissue.news.dto.response.NewsCommentResponseDto;
import com.tave.alarmissue.news.exceptions.NewsCommentErrorCode;
import com.tave.alarmissue.news.exceptions.NewsCommentException;
import com.tave.alarmissue.news.repository.NewsCommentRepository;
import com.tave.alarmissue.news.repository.NewsRepository;
import com.tave.alarmissue.news.repository.NewsVoteRepository;
import com.tave.alarmissue.user.domain.UserEntity;
import com.tave.alarmissue.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.tave.alarmissue.news.exceptions.NewsCommentErrorCode.*;



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
        UserEntity user=userRepository.findById(userId).orElseThrow(()->new NewsCommentException(USER_ID_NOT_FOUND,"사용자가 없습니다."));
        News news=newsRepository.findById(newsId).orElseThrow(()->new NewsCommentException(NEWS_ID_NOT_FOUND,"newsId:"+newsId));


        NewsVoteType newsVoteType = newsVoteRepository.findVoteTypeByNewsIdAndUserId(newsId,userId).orElse(null);
        NewsComment newsComment=newsCommentConverter.toComment(dto,user,news,newsVoteType);
        NewsComment saved=newsCommentRepository.save(newsComment);
        news.incrementCommentCount();
//       newsRepository.save(news);
//        Long totalCommentCount=newsCommentRepository.countByNewsId(newsId);

        return NewsCommentConverter.toCommentResponseDto(saved,newsVoteType);
    }

    @Transactional
    public NewsCommentListResponseDto getCommentsByNewsId(Long newsId,Long userId) {
        List<NewsComment> comments=newsCommentRepository.findByNewsIdOrderByCreatedAtDesc(newsId);
        News news = newsRepository.findById(newsId).orElseThrow(() -> new NewsCommentException(NEWS_ID_NOT_FOUND, "뉴스를 찾을 수 없습니다."));

        Long totalCount=news.getCommentNum();
        NewsVoteType currentUserVoteType=newsVoteRepository.findVoteTypeByNewsIdAndUserId(newsId,userId).orElse(null);

        return NewsCommentConverter.toCommentListResponseDto(newsId,totalCount,comments,currentUserVoteType);
    }

    @Transactional
    public void deleteComment(Long commentId, Long userId) {
        // 해당 뉴스의 해당 사용자가 작성한 댓글만 조회
        NewsComment comment = newsCommentRepository.findByIdAndUserId(commentId, userId)
                .orElseThrow(() -> new NewsCommentException(COMMENT_ID_NOT_FOUND,
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
                .orElseThrow(() -> new NewsCommentException(COMMENT_ID_NOT_FOUND,
                        "해당 뉴스에서 본인이 작성한 댓글을 찾을 수 없습니다."));

        //댓글 내용 업데이트
        comment.updateContent(dto.getComment().trim());
        NewsVoteType currentUserVoteType = newsVoteRepository
                .findVoteTypeByNewsIdAndUserId(dto.getNewsId(), userId)
                .orElse(null);

        return NewsCommentConverter.toCommentResponseDto(comment,currentUserVoteType);

    }
}
