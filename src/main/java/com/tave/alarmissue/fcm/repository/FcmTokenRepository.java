package com.tave.alarmissue.fcm.repository;


import com.tave.alarmissue.fcm.entity.FcmToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.User;

import java.util.List;
import java.util.Optional;

public interface FcmTokenRepository extends JpaRepository<FcmToken, Long> {
    Optional<FcmToken> findByFcmToken(String token);
    List<FcmToken> findByUser(User user);
    void deleteByFcmToken(String token);

    boolean existsByFcmToken(String token);
    boolean existsByFcmTokenAndUserId(String fcmToken, Long userId);

}
