package com.tave.alarmissue.fcm.service;


import com.tave.alarmissue.fcm.entity.FcmToken;
import com.tave.alarmissue.fcm.repository.FcmTokenRepository;
import com.tave.alarmissue.user.domain.UserEntity;
import com.tave.alarmissue.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class FcmTokenService {

    private final FcmTokenRepository fcmTokenRepository;
    private final UserRepository userRepository;

    public FcmTokenService(FcmTokenRepository fcmTokenRepository, UserRepository userRepository) {
        this.fcmTokenRepository = fcmTokenRepository;
        this.userRepository = userRepository;
    }

    /**
     * 토큰 등록/갱신
     */
    @Transactional
    public void registerToken(Long userId, String token) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        FcmToken fcmToken = fcmTokenRepository.findByFcmToken(token)
                .orElseGet(() -> FcmToken.builder()
                        .user(user)
                        .fcmToken(token)
                        .build());

        // 이미 존재하는 토큰이면 user만 갱신
        fcmToken.updateUser(user);

        fcmTokenRepository.save(fcmToken);
    }
    /**
     * 토큰 삭제 (예: 만료/삭제된 토큰)
     */
    @Transactional
    public void deleteToken(String token) {
        fcmTokenRepository.deleteByToken(token);
    }
}