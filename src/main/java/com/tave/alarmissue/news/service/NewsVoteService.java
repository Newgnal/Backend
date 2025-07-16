package com.tave.alarmissue.news.service;

import com.tave.alarmissue.news.domain.News;
import com.tave.alarmissue.news.converter.NewsVoteConverter;
import com.tave.alarmissue.news.domain.NewsComment;
import com.tave.alarmissue.news.domain.NewsVote;
import com.tave.alarmissue.news.domain.enums.NewsVoteType;
import com.tave.alarmissue.news.dto.request.NewsVoteRequestDto;
import com.tave.alarmissue.news.dto.response.NewsVoteCountResponse;
import com.tave.alarmissue.news.dto.response.NewsVoteResponseDto;
import com.tave.alarmissue.news.repository.NewsCommentRepository;
import com.tave.alarmissue.news.repository.NewsVoteRepository;
import com.tave.alarmissue.news.repository.NewsRepository;
import com.tave.alarmissue.news.exceptions.NewsException;
import com.tave.alarmissue.user.domain.UserEntity;
import com.tave.alarmissue.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.tave.alarmissue.news.exceptions.NewsErrorCode.*;


@Service
@RequiredArgsConstructor
public class NewsVoteService {

    private final NewsVoteRepository newsVoteRepository;
    private final NewsRepository newsRepository;
    private final UserRepository userRepository;
    private final NewsCommentRepository newsCommentRepository;

    @Transactional
    public NewsVoteResponseDto createVoteAndGetResult(NewsVoteRequestDto dto, Long userId) {
        //유저가 없을때
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new NewsException(USER_ID_NOT_FOUND, "해당 유저를 찾을 수 없습니다."));

        //게시글이 없을때
        News news = newsRepository.findById(dto.getNewsId()).
                orElseThrow(() -> new NewsException(NEWS_ID_NOT_FOUND, "NewsId: "+ dto.getNewsId()));

        Optional<NewsVote> existingVoteOpt = newsVoteRepository.findByNewsAndUser(news, user);

        NewsVote vote;
        if (existingVoteOpt.isPresent()) {
            vote = existingVoteOpt.get();
            if (vote.getVoteType() != dto.getVoteType()) {
                vote.updateVoteType(dto.getVoteType());
            }
        } else {
            vote = NewsVote.builder()
                    .news(news)
                    .user(user)
                    .voteType(dto.getVoteType())
                    .build();
        }

        newsVoteRepository.save(vote);

        newsVoteRepository.flush();

        //DB에 update
        updateCommentsVoteType(dto.getNewsId(),userId, dto.getVoteType());

        //DB 접근을 최소화
        List<NewsVoteCountResponse> voteCounts = newsVoteRepository.countVotesByType(news);

        NewsVoteResponseDto response = NewsVoteConverter.toVoteResponseDto(news, vote.getVoteType(), voteCounts);

        return response;
    }

    private void updateCommentsVoteType(Long newsId, Long userId, NewsVoteType voteType) {
        newsCommentRepository.updateVoteTypeByNewsIdAndUserId(voteType, newsId, userId);
    }
}
