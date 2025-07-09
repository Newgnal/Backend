package com.tave.alarmissue.post.converter;

import com.tave.alarmissue.post.domain.Post;
import com.tave.alarmissue.post.domain.VoteType;
import com.tave.alarmissue.post.dto.response.VoteCountResponse;
import com.tave.alarmissue.post.dto.response.VoteResponseDto;

import java.util.List;

public class VoteConverter {

    public static VoteResponseDto toVoteResponseDto(Post post, VoteType myVoteType,
                                                    List<VoteCountResponse> counts) {
        int buyCount = 0, sellCount = 0, holdCount = 0;

        for (VoteCountResponse c : counts) {
            switch (c.getVoteType()) {
                case BUY -> buyCount = (int) c.getCount();
                case SELL -> sellCount = (int) c.getCount();
                case HOLD -> holdCount = (int) c.getCount();
            }
        }

        return VoteResponseDto.builder()
                .postId(post.getPostId())
                .buyCount(buyCount)
                .sellCount(sellCount)
                .holdCount(holdCount)
                .voteType(myVoteType)
                .build();
    }
}
