package com.tave.alarmissue.auth.converter;

import com.tave.alarmissue.auth.dto.response.SocialLoginResponse;
import com.tave.alarmissue.auth.dto.response.JwtLoginResponse;
import com.tave.alarmissue.user.domain.UserEntity;



public class AuthConverter {

    public static SocialLoginResponse toSocialLoginResponse(UserEntity userEntity, String accessToken) {
        return SocialLoginResponse.builder()
                .userId(userEntity.getId())
                .email(userEntity.getEmail())
                .nickName(userEntity.getNickName())
                .KakaoAccessToken(accessToken)
                .build();
    }


    public static JwtLoginResponse toJwtLoginResponse(SocialLoginResponse socialLoginResponse, String jwtToken) {
        return JwtLoginResponse.builder()
                .userId(socialLoginResponse.getUserId())
                .email(socialLoginResponse.getEmail())
                .nickName(socialLoginResponse.getNickName())
                .jwtAccessToken(jwtToken)
                .build();
    }


}
