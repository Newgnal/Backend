package com.tave.alarmissue.news.service;

import com.tave.alarmissue.news.converter.NewsCommentConverter;
import com.tave.alarmissue.news.domain.News;
import com.tave.alarmissue.news.domain.NewsComment;
import com.tave.alarmissue.news.domain.enums.NewsVoteType;
import com.tave.alarmissue.news.dto.request.NewsCommentCreateRequestDto;
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
    public NewsCommentResponseDto createComment(NewsCommentCreateRequestDto dto, Long userId, Long newsId){
        UserEntity user=userRepository.findById(userId).orElseThrow(()->new NewsCommentException(USER_ID_NOT_FOUND,"사용자가 없습니다."));
        News news=newsRepository.findById(newsId).orElseThrow(()->new NewsCommentException(NEWS_ID_NOT_FOUND,"newsId:"+newsId));


        NewsVoteType newsVoteType = newsVoteRepository.findVoteTypeByNewsIdAndUserId(newsId,userId).orElse(null);
        NewsComment newsComment=newsCommentConverter.toComment(dto,user,news,newsVoteType);
        NewsComment saved=newsCommentRepository.save(newsComment);
        news.incrementCommentCount();
//       newsRepository.save(news);
//        Long totalCommentCount=newsCommentRepository.countByNewsId(newsId);

        return NewsCommentConverter.toCommentResponseDto(saved);
    }

    public NewsCommentListResponseDto getCommentsByNewsId(Long newsId) {
        List<NewsComment> comments=newsCommentRepository.findByNewsIdOrderByCreatedAtDesc(newsId);
        News news = newsRepository.findById(newsId).orElseThrow(() -> new NewsCommentException(NEWS_ID_NOT_FOUND, "뉴스를 찾을 수 없습니다."));

        Long totalCount=news.getCommentNum();

        return NewsCommentConverter.toCommentListResponseDto(newsId,totalCount,comments);
    }


}
