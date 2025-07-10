package com.tave.alarmissue.user.service;

import com.tave.alarmissue.redis.service.RefreshTokenRedisService;
import com.tave.alarmissue.user.domain.UserEntity;
import com.tave.alarmissue.user.dto.response.LogoutResponse;
import com.tave.alarmissue.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final RefreshTokenRedisService refreshTokenRedisService;

    public LogoutResponse logout(Long userId) {
        boolean isDeleted = refreshTokenRedisService.deleteRefreshToken(userId);
        String message = isDeleted ? "Logout successful" : "Logout failed: User not found";
        return new LogoutResponse(message);
    }

    // 회원 탈퇴 (softdelete)
    @Transactional
    public void softDeleteUser(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(USER_ID_NOT_FOUND, "User not found: " + userId));

        userRepository.delete(user);
    }


}
