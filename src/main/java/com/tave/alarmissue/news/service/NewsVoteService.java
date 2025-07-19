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
@Transactional(readOnly = true)
public class NewsVoteService {

    private final NewsVoteRepository newsVoteRepository;
    private final NewsRepository newsRepository;
    private final UserRepository userRepository;
    private final NewsCommentRepository newsCommentRepository;

    @Transactional
    public void createVoteAndGetResult(NewsVoteRequestDto dto, Long userId) {

        UserEntity user = getUser(userId);

        News news = getNews(dto.getNewsId());

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

        //DB에 update
//        updateVoteType(dto.getNewsId(),userId, dto.getVoteType());
        updateCommentsVoteType(dto.getNewsId(),userId, dto.getVoteType());

    }

    @Transactional
    public NewsVoteResponseDto getVoteResult(NewsVoteRequestDto dto, Long userId) {

        News news = getNews(dto.getNewsId());

        UserEntity user = getUser(userId);

        NewsVote vote = getNewsVote(news, user);

        List<NewsVoteCountResponse> voteCounts = newsVoteRepository.countVotesByType(news);
        news.incrementVoteCount();  //총 투표수 증가

        return NewsVoteConverter.toVoteResponseDto(news, vote.getVoteType(), voteCounts);
    }

    private void updateCommentsVoteType(Long newsId, Long userId, NewsVoteType voteType) {
        // 모든 댓글을 조회해서 업데이트
        List<NewsComment> userComments = newsCommentRepository.findByNewsIdAndUserId(newsId, userId);
        userComments.forEach(comment -> comment.updateVoteType(voteType));
    }

    /*
    private method 분리
     */
    private void updateVoteType(Long newsId, Long userId, NewsVoteType voteType) {
        newsVoteRepository.updateVoteTypeByNewsIdAndUserId(voteType, newsId, userId);
    }

    private UserEntity getUser(Long userId){
        return userRepository.findById(userId)
                .orElseThrow(() -> new NewsException(USER_ID_NOT_FOUND, "UserId"+userId));
    }

    private News getNews(Long newsId){
        return newsRepository.findById(newsId).
                orElseThrow(() -> new NewsException(NEWS_ID_NOT_FOUND, "NewsId: "+ newsId));

    }

    private NewsVote getNewsVote(News news, UserEntity user) {
        return newsVoteRepository.findByNewsAndUser(news, user)
                .orElseThrow(() -> new NewsException(VOTE_NOT_FOUND));
    }
}
