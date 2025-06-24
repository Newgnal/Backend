package com.tave.alarmissue.user.service;

import com.tave.alarmissue.redis.service.RefreshTokenRedisService;
import com.tave.alarmissue.user.dto.response.LogoutResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final RefreshTokenRedisService refreshTokenRedisService;

    public LogoutResponse logout(String userId) {
        boolean isDeleted = refreshTokenRedisService.deleteRefreshToken(Long.parseLong(userId));
        String message = isDeleted ? "Logout successful" : "Logout failed: User not found";
        return new LogoutResponse(message);
    }

}
