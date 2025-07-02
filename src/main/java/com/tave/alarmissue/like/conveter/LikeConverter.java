package com.tave.alarmissue.like.conveter;

import com.tave.alarmissue.like.domain.Like;
import com.tave.alarmissue.like.dto.request.LikeRequestCreateDto;
import com.tave.alarmissue.like.dto.response.LikeResponseDto;
import com.tave.alarmissue.user.domain.UserEntity;
import lombok.Builder;
import org.springframework.stereotype.Component;

@Component
@Builder
public class LikeConverter {
public  Like toLike(LikeRequestCreateDto dto, UserEntity user){
    return Like.builder().
            liked(dto.isLiked()).
            user(user).
            build();
}

public static LikeResponseDto toLikeResponseDto(Like like){
    return LikeResponseDto.builder()
            .liked(like.isLiked())
            .build();


}

}
