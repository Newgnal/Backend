package com.tave.alarmissue.auth.service;

import com.tave.alarmissue.auth.client.KakaoApiClient;
import com.tave.alarmissue.auth.converter.AuthConverter;
import com.tave.alarmissue.auth.dto.request.RegisterRequest;
import com.tave.alarmissue.auth.dto.request.TokenRequest;
import com.tave.alarmissue.auth.dto.response.KakaoUserInfo;
import com.tave.alarmissue.auth.dto.response.SocialLoginResponse;
import com.tave.alarmissue.redis.service.RefreshTokenRedisService;
import com.tave.alarmissue.security.jwt.JwtProvider;
import com.tave.alarmissue.user.domain.UserEntity;
import com.tave.alarmissue.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final KakaoApiClient kakaoApiClient;
    private final JwtProvider jwtProvider;

    private final RefreshTokenRedisService refreshTokenRedisService;
    @Value("${jwt.expiration.refresh}")
    private Long REFRESH_TOKEN_EXPIRE_TIME;

    public SocialLoginResponse loginOrRegister(TokenRequest tokenRequest) {

        String accessToken = tokenRequest.getAccessToken();

        // 카카오 리소스 서버에 사용자 정보 요청
        KakaoUserInfo kakaoUserInfo = kakaoApiClient.getUserInfo(accessToken);
        log.debug("카카오 사용자 정보: {}", kakaoUserInfo);

        // 사용자 정보 가져오기
        String email = kakaoUserInfo.getKakaoAccount().getEmail();
        String profileImage = kakaoUserInfo.getKakaoAccount().getProfile().getProfileImageUrl();

        //닉네임 자동 생성
        UserEntity user = userRepository.findByEmail(email)
                .orElseGet(() -> {
                    String kakaoNickname = kakaoUserInfo.getKakaoAccount().getProfile().getNickname();
                    String nicknameToUse = (kakaoNickname != null && !kakaoNickname.isBlank())
                            ? kakaoNickname
                            : generateRandomNickname();

                    UserEntity newUser = UserEntity.builder()
                            .email(email)
                            .nickName(nicknameToUse)
                            .imageUrl(profileImage)
                            .enabled(false)
                            .build();
                    return userRepository.save(newUser);
                });
        // AuthConverter 수정: accessToken 포함 반환
        return AuthConverter.toSocialLoginResponse(user,accessToken);
    }

    private String generateRandomNickname() {
        return "user_" + UUID.randomUUID().toString().substring(0, 8);
    }

    public String createAccessTokenWhenLogin(Long userId) {

        Authentication authentication = jwtProvider.getAuthenticationFromUserId(userId.toString());
        String accessToken = jwtProvider.generateAccessToken(authentication, userId.toString());
        String refreshToken = jwtProvider.generateRefreshToken(authentication, userId.toString());

        refreshTokenRedisService.saveRefreshToken(userId, refreshToken, REFRESH_TOKEN_EXPIRE_TIME);

        return "Bearer " + accessToken;
    }

}