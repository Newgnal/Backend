package com.tave.alarmissue.auth.controller;

import com.tave.alarmissue.auth.converter.AuthConverter;
import com.tave.alarmissue.auth.dto.response.JwtLoginResponse;
import com.tave.alarmissue.redis.service.RefreshTokenRedisService;
import com.tave.alarmissue.security.jwt.JwtProvider;
import com.tave.alarmissue.user.domain.UserEntity;
import com.tave.alarmissue.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/test")
@RequiredArgsConstructor
@Slf4j
public class TestController {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final RefreshTokenRedisService refreshTokenRedisService;

    @Value("${jwt.expiration.refresh}")
    private Long REFRESH_TOKEN_EXPIRE_TIME;

    /**
     * 테스트용 토큰 발급 (user@test.com)
     */
    @PostMapping("/token")
    public ResponseEntity<JwtLoginResponse> generateTestToken() {
        try {
            // 1. 테스트 사용자 조회 또는 생성
            UserEntity user = userRepository.findByEmail("user@test.com")
                    .orElseGet(() -> {
                        log.info("테스트 사용자 생성: user@test.com");
                        return userRepository.save(UserEntity.builder()
                                .email("user@test.com")
                                .nickName("테스트유저")
                                .imageUrl("https://example.com/test-profile.jpg")
                                .enabled(true)
                                .build());
                    });

            // 2. JWT 토큰 생성
            String userIdStr = user.getId().toString();
            Authentication authentication = jwtProvider.getAuthenticationFromUserId(userIdStr);
            String accessToken = jwtProvider.generateAccessToken(authentication, userIdStr);
            String refreshToken = jwtProvider.generateRefreshToken(authentication, userIdStr);

            // 3. Refresh Token을 Redis에 저장
            refreshTokenRedisService.saveRefreshToken(userIdStr, refreshToken, REFRESH_TOKEN_EXPIRE_TIME);

            // 4. 응답 생성
            JwtLoginResponse response = AuthConverter.toJwtLoginResponse(user, accessToken, refreshToken);

            log.info("테스트 토큰 발급 완료 - userId: {}, email: {}", user.getId(), user.getEmail());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("테스트 토큰 발급 실패: " + e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}