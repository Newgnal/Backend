package com.tave.alarmissue.news.service;

import com.tave.alarmissue.news.converter.NewsCommentConverter;
import com.tave.alarmissue.news.domain.News;
import com.tave.alarmissue.news.domain.NewsComment;
import com.tave.alarmissue.news.domain.enums.NewsVoteType;
import com.tave.alarmissue.news.dto.request.NewsCommentCreateRequestDto;
import com.tave.alarmissue.news.dto.response.NewsCommentCountResponseDto;
import com.tave.alarmissue.news.dto.response.NewsCommentCreateResponseDto;
import com.tave.alarmissue.news.dto.response.NewsCommentResponseDto;
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
    private final NewsCommentConverter newsCommentConveter;

    @Transactional
    public NewsCommentCreateResponseDto createComment(NewsCommentCreateRequestDto dto, Long userId, Long newsId){
        UserEntity user=userRepository.findById(userId).orElseThrow(()->new NewsCommentException(USER_ID_NOT_FOUND,"사용자가 없습니다."));
        News news=newsRepository.findById(newsId).orElseThrow(()->new NewsCommentException(NEWS_ID_NOT_FOUND,"newsId:"+newsId));


        NewsVoteType newsVoteType = newsVoteRepository.findVoteTypeByNewsIdAndUserId(newsId,userId).orElse(null);
        NewsComment newsComment=newsCommentConveter.toComment(dto,user,news,newsVoteType);
        NewsComment saved=newsCommentRepository.save(newsComment);

        Long totalCommentCount=newsCommentRepository.countByNewsId(newsId);

        return NewsCommentConverter.toCommentCreateResponseDto(saved,totalCommentCount);
    }

    public List<NewsCommentResponseDto> getCommentsByNewsId(Long newsId) {
        List<NewsComment> comments=newsCommentRepository.findByNewsIdOrderByCreatedAtDesc(newsId);
        return comments.stream()
                .map(NewsCommentConverter::toCommentResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteComment(Long commentId, Long userId, Long newsId) {
        UserEntity user=userRepository.findById(userId).orElseThrow(()->new NewsCommentException(USER_ID_NOT_FOUND,"사용자가 없습니다."));
        News news=newsRepository.findById(newsId).orElseThrow(()->new NewsCommentException(NEWS_ID_NOT_FOUND,"newsId:"+newsId));

        //댓글 존재 확인
        NewsComment comment = newsCommentRepository.findById(commentId)
                .orElseThrow(() -> new NewsCommentException(COMMENT_ID_NOT_FOUND, "댓글을 찾을 수 없습니다."));
        //작성자 본인인지 확인
        if(!comment.getUser().getId().equals(userId)){
            throw new NewsCommentException(UNAUTHORIZED_DELETE,"본인이 작성한 댓글만 삭제할 수 있습니다.");
        }
        //댓글 삭제
        newsCommentRepository.delete(comment);
    }

    @Transactional
    public NewsCommentCountResponseDto deleteCommentAndGetCount(Long commentId, Long userId){
        //댓글 조회
        NewsComment comment=newsCommentRepository.findById(commentId).orElseThrow(()->new NewsCommentException(COMMENT_ID_NOT_FOUND,"댓글을 찾을 수 없습니다."));
        Long newsId=comment.getNews().getId();

        //작성자 본인 확인
        if(!comment.getUser().getId().equals(userId)){
            throw new NewsCommentException(UNAUTHORIZED_DELETE, "본인이 작성한 댓글만 삭제할 수 있습니다.");
        }
        //댓글 삭제
        newsCommentRepository.delete(comment);
        Long updatedCount=newsCommentRepository.countByNewsId(newsId);

        return NewsCommentConverter.toCommentCountResponseDto(newsId,updatedCount);



    }
}
