package com.tave.alarmissue.post.service;

import com.tave.alarmissue.post.domain.Post;
import com.tave.alarmissue.post.repository.PostRepository;
import com.tave.alarmissue.user.domain.UserEntity;
import com.tave.alarmissue.user.repository.UserRepository;
import com.tave.alarmissue.post.converter.PostVoteConverter;
import com.tave.alarmissue.post.domain.PostVote;
import com.tave.alarmissue.post.dto.request.VoteRequestDto;
import com.tave.alarmissue.post.dto.response.VoteCountResponse;
import com.tave.alarmissue.post.dto.response.VoteResponseDto;
import com.tave.alarmissue.post.exception.VoteException;
import com.tave.alarmissue.post.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import static com.tave.alarmissue.post.exception.VoteErrorCode.*;

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
                orElseThrow(() -> new VoteException(POST_ID_NOT_FOUND, "postId: "+dto.getPostId()));

        //게시글에 투표가 없을때
        if (!post.getHasVote()) {
            throw new VoteException(POST_NOT_VOTABLE, "postId: "+ dto.getPostId());
        }

        // 기존 투표 조회
        PostVote newVote = voteRepository.findByUserAndPost(user, post)
                .orElse(null);

        if (newVote != null) {
            // 기존 투표가 있다면 수정
            newVote.updateVoteType(dto.getVoteType());
        } else {
            //없으면 새로 생성
            newVote = PostVote.builder()
                    .user(user)
                    .post(post)
                    .voteType(dto.getVoteType())
                    .build();
        }
        voteRepository.save(newVote);
        //DB 접근을 최소화
        List<VoteCountResponse> voteCounts = voteRepository.countVotesByType(post);

        VoteResponseDto response = PostVoteConverter.toVoteResponseDto(post, newVote.getVoteType(), voteCounts);

        return response;
    }
}
