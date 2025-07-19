package com.tave.alarmissue.news.service;

import com.tave.alarmissue.news.converter.NewsCommentConverter;
import com.tave.alarmissue.news.domain.News;
import com.tave.alarmissue.news.domain.NewsComment;
import com.tave.alarmissue.news.domain.enums.NewsVoteType;
import com.tave.alarmissue.news.dto.request.NewsCommentRequestDto;
import com.tave.alarmissue.news.dto.request.NewsCommentUpdateRequest;
import com.tave.alarmissue.news.dto.request.NewsReplyRequest;
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

        List<NewsComment> comments = newsCommentRepository.findByNewsIdAndParentCommentIsNullOrderByCreatedAtDesc(newsId);
        News news = getNewsById(newsId);

        Long totalCount = news.getCommentNum();

        return NewsCommentConverter.toCommentListResponseDto(newsId, totalCount, comments);
    }


    //댓글,답글 삭제(어처피 Id로 삭제하는 것이기 때문!)
    @Transactional
    public void deleteComment(Long commentId, Long userId) {

        // 해당 뉴스의 해당 사용자가 작성한 댓글만 조회
        NewsComment comment = getNewsCommentById(commentId,userId);

        News news = comment.getNews();

        //원댓글 삭제
        if(comment.getParentComment()==null){
            //본인+답글들 삭제
            int totalDeletedCount = 1 + comment.getReplies().size();
            news.decrementCommentCountBy(totalDeletedCount);
        }
        else {
            // 답글만 삭제
            news.decrementCommentCount();
        }
        newsCommentRepository.delete(comment);
    }

    //댓글,답글 수정
    @Transactional
    public NewsCommentResponseDto updateComment(Long commentId, Long userId, NewsCommentUpdateRequest dto) {

        NewsComment comment = getNewsCommentById(commentId,userId);

        //댓글 내용 업데이트
        comment.updateContent(dto.getComment().trim());

        return NewsCommentConverter.toCommentResponseDto(comment);

    }



    //답글 작성
    @Transactional
    public NewsCommentResponseDto createReply(Long userId, NewsReplyRequest dto) {
        UserEntity user = getUserById(userId);   //사용자 확인
        NewsComment parentComment = getNewsCommentById(dto.getParentId(),userId); //부모댓글 확인
        News news= parentComment.getNews();

        //답글은 부모 댓글id가 있으니까 부모 댓글 id가 있으면 답글 달 수 없게
        if(isReply(parentComment)){
            throw new NewsException(INVALID_REQUEST, "대댓글에는 답글을 달 수 없습니다.");
        }

        NewsVoteType newsVoteType = newsVoteRepository.findVoteTypeByNewsIdAndUserId(news.getId(), userId).orElse(null);
        NewsComment reply=NewsComment.builder()
                .comment(dto.getComment())
                .user(user)
                .news(news)
                .parentComment(parentComment)
                .voteType(newsVoteType)
                .build();
        NewsComment savedReply = newsCommentRepository.save(reply);

        news.incrementCommentCount();

        return NewsCommentConverter.toCommentResponseDto(savedReply);
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

    private boolean isReply(NewsComment comment){
        return comment.getParentComment()!=null;
    }



}