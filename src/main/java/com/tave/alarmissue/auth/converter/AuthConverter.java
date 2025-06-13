package com.tave.alarmissue.auth.converter;

import com.tave.alarmissue.auth.dto.response.SocialLoginResponse;
import com.tave.alarmissue.auth.dto.response.JwtLoginResponse;
import com.tave.alarmissue.user.domain.UserEntity;



public class AuthConverter {


    public static JwtLoginResponse toJwtLoginResponse(UserEntity user, String accessToken, String refreshToken) {
        return JwtLoginResponse.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .nickName(user.getNickName())
                .jwtAccessToken("Bearer " + accessToken)
                .jwtRefreshToken(refreshToken)
                .build();
    }


}
