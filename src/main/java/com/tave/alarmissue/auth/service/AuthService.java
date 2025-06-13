package com.tave.alarmissue.auth.service;

import com.tave.alarmissue.auth.client.KakaoApiClient;
import com.tave.alarmissue.auth.converter.AuthConverter;
import com.tave.alarmissue.auth.dto.response.JwtLoginResponse;
import com.tave.alarmissue.auth.dto.response.KakaoUserInfo;
import com.tave.alarmissue.auth.dto.response.SocialLoginResponse;
import com.tave.alarmissue.redis.entity.RefreshToken;
import com.tave.alarmissue.redis.service.RefreshTokenRedisService;
import com.tave.alarmissue.security.exception.SecurityErrorCode;
import com.tave.alarmissue.security.exception.TokenException;
import com.tave.alarmissue.security.jwt.JwtProvider;
import com.tave.alarmissue.user.domain.UserEntity;
import com.tave.alarmissue.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final KakaoApiClient kakaoApiClient;
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final RefreshTokenRedisService refreshTokenRedisService;

    @Value("${jwt.expiration.refresh}")
    private Long REFRESH_TOKEN_EXPIRE_TIME;

    public JwtLoginResponse loginOrRegister(String code) {

        // 1. 인가 코드로 카카오 access token 요청
        String kakaoAccessToken = kakaoApiClient.requestAccessToken(code);

        // 2. 카카오 access token으로 사용자 정보 조회
        KakaoUserInfo kakaoUserInfo = kakaoApiClient.getUserInfo(kakaoAccessToken);
        String email = kakaoUserInfo.getKakaoAccount().getEmail();
        String profileImage = kakaoUserInfo.getKakaoAccount().getProfile().getProfileImageUrl();

        // 3. 회원 조회 또는 생성
        UserEntity user = userRepository.findByEmail(email)
                .orElseGet(() -> {
                    String nicknameToUse = generateRandomNickname();
                    return userRepository.save(UserEntity.builder()
                            .email(email)
                            .nickName(nicknameToUse)
                            .imageUrl(profileImage)
                            .enabled(false)
                            .build());
                });

        // 4. Access & Refresh Token 생성
        Authentication authentication = jwtProvider.getAuthenticationFromUserId(user.getId().toString());
        String accessToken = jwtProvider.generateAccessToken(authentication, user.getId().toString());
        String refreshToken = jwtProvider.generateRefreshToken(authentication, user.getId().toString());

        // 5. Refresh Token을 Redis에 저장
        refreshTokenRedisService.saveRefreshToken(user.getId(), refreshToken, REFRESH_TOKEN_EXPIRE_TIME);

        // 6. JwtLoginResponse 반환
        return AuthConverter.toJwtLoginResponse(user,accessToken,refreshToken);
    }


    //닉네임 unique 처리
    private String generateRandomNickname() {
        for (int i = 0; i < 5; i++) {
            String nickname = "user_" + UUID.randomUUID().toString().substring(0, 4);
            boolean exists = userRepository.existsByNickName(nickname);
            if (!exists) {
                return nickname;
            }
        }
        // 최대 시도 횟수 초과 시, 그냥 UUID 전체를 씀
        return "user_" + UUID.randomUUID().toString().replace("-", "");
    }

    //백엔드 확인용
    public String reissueAccessToken(String refreshToken) {

        // refreshToken 유효성 검사
        if (!jwtProvider.validateToken(refreshToken)) {
            throw new TokenException(SecurityErrorCode.REFRESH_EXPIRED);
        }

        // refreshToken에서 userId 추출
        String userId = jwtProvider.getSubject(refreshToken);

        // redis에서 refreshToken 검증
        Optional<RefreshToken> optionalRefresh = refreshTokenRedisService.findRefreshToken(Long.parseLong(userId));

        if (optionalRefresh.isEmpty() || !optionalRefresh.get().getRefreshToken().equals(refreshToken)) {
            throw new TokenException(SecurityErrorCode.REFRESH_EXPIRED);
        }

        // 새 access token 생성
        Authentication authentication = jwtProvider.getAuthenticationFromUserId(userId);
        return jwtProvider.generateAccessToken(authentication, userId);
    }
}
