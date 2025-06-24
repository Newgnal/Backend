package com.tave.alarmissue.vote.service;

import com.tave.alarmissue.post.domain.Post;
import com.tave.alarmissue.post.repository.PostRepository;
import com.tave.alarmissue.user.domain.UserEntity;
import com.tave.alarmissue.vote.domain.Vote;
import com.tave.alarmissue.vote.domain.VoteType;
import com.tave.alarmissue.vote.dto.response.VoteResponseDto;
import com.tave.alarmissue.vote.exception.VoteErrorCode;
import com.tave.alarmissue.vote.exception.VoteException;
import com.tave.alarmissue.vote.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VoteService {

    private final VoteRepository voteRepository;
    private final PostRepository postRepository;

    @Transactional
    public void vote(UserEntity user, Long postId, VoteType voteType) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new VoteException(VoteErrorCode.POST_ID_NOT_FOUND));

        // 기존 투표가 있으면 삭제(재투표 가능하게)
        voteRepository.findByUserAndPost(user, post).ifPresent(existingVote -> {
            voteRepository.delete(existingVote);
            voteRepository.flush();
        });

        // 새 투표 저장
        Vote newVote = Vote.builder()
                .user(user)
                .post(post)
                .voteType(voteType)
                .build();

        voteRepository.save(newVote);
    }
    public VoteResponseDto getVoteCounts(Long postId, UserEntity user){
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new VoteException(VoteErrorCode.POST_ID_NOT_FOUND));

        int buyCount = voteRepository.countByPostAndVoteType(post, VoteType.BUY);
        int sellCount = voteRepository.countByPostAndVoteType(post, VoteType.SELL);
        int holdCount = voteRepository.countByPostAndVoteType(post, VoteType.HOLD);

        Vote myVote = voteRepository.findByUserAndPost(user, post).orElse(null);
        return VoteResponseDto.builder()
                .postId(postId)
                .buyCount(buyCount)
                .sellCount(sellCount)
                .holdCount(holdCount)
                .myvoteType(myVote != null ? myVote.getVoteType() : null)
                .build();
    }
}