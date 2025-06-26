package com.tave.alarmissue.vote.service;

import com.tave.alarmissue.post.domain.Post;
import com.tave.alarmissue.post.exception.PostException;
import com.tave.alarmissue.post.repository.PostRepository;
import com.tave.alarmissue.user.domain.UserEntity;
import com.tave.alarmissue.user.repository.UserRepository;
import com.tave.alarmissue.vote.converter.VoteConverter;
import com.tave.alarmissue.vote.domain.Vote;
import com.tave.alarmissue.vote.domain.VoteType;
import com.tave.alarmissue.vote.dto.request.VoteRequestDto;
import com.tave.alarmissue.vote.dto.response.VoteCountResponse;
import com.tave.alarmissue.vote.dto.response.VoteResponseDto;
import com.tave.alarmissue.vote.exception.VoteErrorCode;
import com.tave.alarmissue.vote.exception.VoteException;
import com.tave.alarmissue.vote.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import static com.tave.alarmissue.vote.exception.VoteErrorCode.*;

@Service
@RequiredArgsConstructor
public class VoteService {

    private final VoteRepository voteRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public VoteResponseDto createVoteAndGetResult(VoteRequestDto dto,Long userId) {

        //유저가 없을때
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new VoteException(USER_ID_NOT_FOUND, "해당 유저를 찾을 수 없습니다."));

        //게시글이 없을때
        Post post = postRepository.findById(dto.getPostId()).
                orElseThrow(() -> new VoteException(POST_ID_NOT_FOUND, "해당 게시글을 찾을 수 없습니다."));

        //게시글에 투표가 없을때
        if (!post.getHasVote()) {
            throw new VoteException(POST_NOT_VOTABLE, "투표 기능이 존재하지 않습니다.");
        }

        // 기존 투표가 있으면 삭제(재투표 가능하게)
        voteRepository.deleteByUserAndPost(user, post);

        // 새 투표 저장
        Vote newVote = Vote.builder()
                .user(user)
                .post(post)
                .voteType(dto.getVoteType())
                .build();
        voteRepository.save(newVote);

        //DB 접근을 최소화
        List<VoteCountResponse> voteCounts = voteRepository.countVotesByType(post);

        VoteResponseDto response = VoteConverter.toVoteResponseDto(post, newVote.getVoteType(), voteCounts);

        return response;
    }
}
