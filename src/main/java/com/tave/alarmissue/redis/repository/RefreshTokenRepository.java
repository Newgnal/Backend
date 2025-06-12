package com.tave.alarmissue.redis.repository;

import com.tave.alarmissue.redis.entity.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long> {
}
