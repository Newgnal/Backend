package com.tave.alarmissue.auth.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SocialLoginResponse {
    private Long userId;
    private String email;
    private String nickName;
    private String KakaoAccessToken;
     // boolean enabled;
}
