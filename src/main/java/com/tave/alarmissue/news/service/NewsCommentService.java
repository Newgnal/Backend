package com.tave.alarmissue.news.service;

import com.tave.alarmissue.news.converter.NewsCommentConverter;
import com.tave.alarmissue.news.domain.News;
import com.tave.alarmissue.news.domain.NewsComment;
import com.tave.alarmissue.news.domain.enums.NewsVoteType;
import com.tave.alarmissue.news.dto.request.NewsCommentRequestDto;
import com.tave.alarmissue.news.dto.response.NewsCommentListResponseDto;
import com.tave.alarmissue.news.dto.response.NewsCommentResponseDto;
import com.tave.alarmissue.news.exceptions.NewsException;
import com.tave.alarmissue.news.repository.NewsCommentRepository;
import com.tave.alarmissue.news.repository.NewsRepository;
import com.tave.alarmissue.news.repository.NewsVoteRepository;
import com.tave.alarmissue.post.domain.Post;
import com.tave.alarmissue.post.exception.PostException;
import com.tave.alarmissue.user.domain.UserEntity;
import com.tave.alarmissue.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.tave.alarmissue.news.exceptions.NewsErrorCode.*;
import static com.tave.alarmissue.post.exception.PostErrorCode.POST_ID_NOT_FOUND;
import static com.tave.alarmissue.post.exception.PostErrorCode.USER_ID_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NewsCommentService {

    private final NewsCommentRepository newsCommentRepository;
    private final UserRepository userRepository;
    private final NewsRepository newsRepository;
    private final NewsVoteRepository newsVoteRepository;
    private final NewsCommentConverter newsCommentConverter;

    //뉴스 댓글 작성
    @Transactional
    public NewsCommentResponseDto createComment(NewsCommentRequestDto dto, Long userId, Long newsId) {

        UserEntity user = getUserById(userId);
        News news = getNewsById(newsId);


        NewsVoteType newsVoteType = newsVoteRepository.findVoteTypeByNewsIdAndUserId(newsId, userId).orElse(null);
        NewsComment newsComment = newsCommentConverter.toComment(dto, user, news, newsVoteType);
        NewsComment saved = newsCommentRepository.save(newsComment);
        news.incrementCommentCount();

        return NewsCommentConverter.toCommentResponseDto(saved);
    }

    // 뉴스 댓글 반환
    public NewsCommentListResponseDto getCommentsByNewsId(Long newsId) {

        List<NewsComment> comments = newsCommentRepository.findByNewsIdOrderByCreatedAtDesc(newsId);
        News news = getNewsById(newsId);

        Long totalCount = news.getCommentNum();

        return NewsCommentConverter.toCommentListResponseDto(newsId, totalCount, comments);
    }


    //댓글 삭제
    @Transactional
    public void deleteComment(Long commentId, Long userId) {

        // 해당 뉴스의 해당 사용자가 작성한 댓글만 조회
        NewsComment comment = getNewsCommentById(commentId,userId);

        News news = comment.getNews();

        // 댓글 삭제
        newsCommentRepository.delete(comment);

        // 댓글 개수 감소
        news.decrementCommentCount();
    }

    @Transactional
    public NewsCommentResponseDto updateComment(Long commentId, Long userId, NewsCommentRequestDto dto) {

        NewsComment comment = getNewsCommentById(commentId,userId);

        //댓글 내용 업데이트
        comment.updateContent(dto.getComment().trim());

        return NewsCommentConverter.toCommentResponseDto(comment);

    }


    /*
 private method 분리
  */
    private UserEntity getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NewsException(USER_ID_NOT_FOUND, "userId"+userId));
    }

    private News getNewsById(Long newsId) {
        return newsRepository.findById(newsId)
                .orElseThrow(() -> new NewsException(NEWS_ID_NOT_FOUND, "newsId"+newsId));
    }

    private NewsComment getNewsCommentById(Long commentId, Long userId){
        return newsCommentRepository.findByIdAndUserId(commentId,userId)
                .orElseThrow(() -> new NewsException(COMMENT_ID_NOT_FOUND,"userId"+userId));
    }


}