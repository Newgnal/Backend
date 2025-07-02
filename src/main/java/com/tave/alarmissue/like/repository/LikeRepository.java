package com.tave.alarmissue.like.repository;

import com.tave.alarmissue.like.domain.Like;
import org.springframework.data.jpa.repository.JpaRepository;


public interface LikeRepository extends JpaRepository<Like, Long> {
}
