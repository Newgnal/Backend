package com.tave.alarmissue.notification.repository;

import com.tave.alarmissue.notification.domain.UserNotificationSetting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserNotificationSettingRepository extends JpaRepository<UserNotificationSetting, Long> {

    Optional<UserNotificationSetting> findByUserId(Long userId);

    boolean existsByUserId(Long userId);
}

