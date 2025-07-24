package com.tave.alarmissue.fcm.service;


import com.tave.alarmissue.fcm.dto.response.FcmTokenResponse;
import com.tave.alarmissue.fcm.entity.FcmToken;
import com.tave.alarmissue.fcm.exception.FcmErrorCode;
import com.tave.alarmissue.fcm.exception.FcmException;
import com.tave.alarmissue.fcm.repository.FcmTokenRepository;
import com.tave.alarmissue.user.domain.UserEntity;
import com.tave.alarmissue.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FcmTokenService {

    private final FcmTokenRepository fcmTokenRepository;
    private final UserRepository userRepository;

    /**
     * 토큰 등록/갱신
     */
    @Transactional
    public FcmTokenResponse registerToken(Long userId, String token) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() ->  new FcmException(FcmErrorCode.USER_NOT_FOUND));

        FcmToken fcmToken = fcmTokenRepository.findByFcmToken(token)
                .orElseGet(() -> FcmToken.builder()
                        .user(user)
                        .fcmToken(token)
                        .build());

        // 이미 존재하는 토큰이면 user만 갱신
        fcmToken.updateUser(user);

        FcmToken savedToken = fcmTokenRepository.save(fcmToken);

        return FcmTokenResponse.builder()
                .fcmToken(savedToken.getFcmToken())
                .userId(savedToken.getUser().getId())
                .registeredAt(savedToken.getUpdatedAt()) // BaseTimeEntity의 updatedAt 사용
                .build();
    }
    /**
     * 토큰 삭제 (예: 만료/삭제된 토큰)
     */
    @Transactional
    public void deleteToken(String token) {
        if (!fcmTokenRepository.existsByFcmToken(token)) {
            throw new FcmException(FcmErrorCode.TOKEN_NOT_FOUND);
        }

        fcmTokenRepository.deleteByFcmToken(token);
    }

    @Transactional(readOnly = true)
    public boolean isTokenExists(String token) {
        return fcmTokenRepository.existsByFcmToken(token);
    }

    @Transactional(readOnly = true)
    public UserEntity getUserByToken(String token) {
        return fcmTokenRepository.findByFcmToken(token)
                .map(FcmToken::getUser)
                .orElseThrow(() -> new FcmException(FcmErrorCode.TOKEN_NOT_FOUND));
    }

    /**
     * 사용자별 FCM 토큰 목록 조회
     */
    @Transactional(readOnly = true)
    public List<String> getUserTokens(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new FcmException(FcmErrorCode.USER_NOT_FOUND));

        return fcmTokenRepository.findByUser(user)
                .stream()
                .map(FcmToken::getFcmToken)
                .collect(Collectors.toList());
    }
}