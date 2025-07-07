package com.tave.alarmissue.news.service;

import com.tave.alarmissue.news.converter.NewsCommentConverter;
import com.tave.alarmissue.news.domain.News;
import com.tave.alarmissue.news.domain.NewsComment;
import com.tave.alarmissue.news.domain.enums.NewsVoteType;
import com.tave.alarmissue.news.dto.request.NewsCommentCreateRequestDto;
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

import static com.tave.alarmissue.news.exceptions.NewsCommentErrorCode.NEWS_ID_NOT_FOUND;
import static com.tave.alarmissue.news.exceptions.NewsCommentErrorCode.USER_ID_NOT_FOUND;

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
}
