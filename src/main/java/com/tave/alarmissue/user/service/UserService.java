package com.tave.alarmissue.user.service;

import com.tave.alarmissue.redis.service.RefreshTokenRedisService;
import com.tave.alarmissue.user.domain.UserEntity;
import com.tave.alarmissue.user.dto.response.LogoutResponse;
import com.tave.alarmissue.user.dto.response.NicknameResponse;
import com.tave.alarmissue.user.exception.UserException;
import com.tave.alarmissue.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.tave.alarmissue.user.exception.UserErrorCode.NICKNAME_ALREADY_EXISTS;
import static com.tave.alarmissue.user.exception.UserErrorCode.USER_ID_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class UserService {

    private final RefreshTokenRedisService refreshTokenRedisService;
    private final UserRepository userRepository;

    public LogoutResponse logout(Long userId) {
        boolean isDeleted = refreshTokenRedisService.deleteRefreshToken(userId);
        String message = isDeleted ? "Logout successful" : "Logout failed: User not found";
        return new LogoutResponse(message);
    }

    @Transactional
    public NicknameResponse changeNickname(Long userId,String nickname) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(USER_ID_NOT_FOUND,"User not found: " + userId));

        if (userRepository.existsByNickName(nickname)){
            throw new UserException(NICKNAME_ALREADY_EXISTS, "Nickname already in use: " + nickname);
        }

        user.changeNickname(nickname);
        userRepository.save(user);

        return new NicknameResponse( user.getId(), user.getNickName());
    }

}
