package com.tave.alarmissue.post.converter;

import com.tave.alarmissue.post.domain.Post;
import com.tave.alarmissue.post.domain.enums.VoteType;
import com.tave.alarmissue.post.dto.response.VoteCountResponse;
import com.tave.alarmissue.post.dto.response.VoteResponse;

import java.util.List;

public class PostVoteConverter {

    public static VoteResponse toVoteResponseDto(Post post, VoteType myVoteType,
                                                    List<VoteCountResponse> counts) {
        int buyCount = 0, sellCount = 0, holdCount = 0;

        for (VoteCountResponse c : counts) {
            switch (c.getVoteType()) {
                case BUY -> buyCount = (int) c.getCount();
                case SELL -> sellCount = (int) c.getCount();
                case HOLD -> holdCount = (int) c.getCount();
            }
        }

        return VoteResponse.builder()
                .postId(post.getPostId())
                .buyCount(buyCount)
                .sellCount(sellCount)
                .holdCount(holdCount)
                .voteType(myVoteType)
                .build();
    }
}
