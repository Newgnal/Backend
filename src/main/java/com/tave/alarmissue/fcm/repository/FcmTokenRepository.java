package com.tave.alarmissue.fcm.repository;


import com.tave.alarmissue.fcm.entity.FcmToken;
import com.tave.alarmissue.user.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Optional;

public interface FcmTokenRepository extends JpaRepository<FcmToken, Long> {
    Optional<FcmToken> findByFcmToken(String token);
    List<FcmToken> findByUser(UserEntity user);
    void deleteByFcmToken(String token);

    boolean existsByFcmToken(String token);
    boolean existsByFcmTokenAndUserId(String fcmToken, Long userId);

}
