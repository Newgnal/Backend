package com.tave.alarmissue.news.poll.service;

import com.tave.alarmissue.news.domain.News;
import com.tave.alarmissue.news.poll.domain.NewsVote;
import com.tave.alarmissue.news.poll.domain.NewsVoteType;
import com.tave.alarmissue.news.poll.dto.request.NewsVoteRequestDto;
import com.tave.alarmissue.news.poll.dto.response.NewsVoteResponseDto;
import com.tave.alarmissue.news.poll.repository.NewsVoteRepository;
import com.tave.alarmissue.news.repository.NewsRepository;
import com.tave.alarmissue.user.domain.UserEntity;
import com.tave.alarmissue.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NewsVoteService {

    private final NewsVoteRepository newsVoteRepository;
    private final NewsRepository newsRepository;
    private final UserRepository userRepository;

    @Transactional
    public NewsVoteResponseDto createVoteAndGetResult(NewsVoteRequestDto dto, Long userId) {
        UserEntity user=userRepository.findById(userId).orElseThrow(()->new IllegalArgumentException("유저 없음"));
        News news=newsRepository.findById(dto.getNewsId()).orElseThrow(()->new IllegalArgumentException("뉴스 없음"));
        NewsVote vote=NewsVote.builder()
                .news(news)
                .user(user)
                .voteType(dto.getVoteType())
                .question("이 뉴스가 ["+news.getThema()+"]에 어떤 영향을 줄까요?")
                .build();
        newsVoteRepository.save(vote);

        return null;
    }
}
