package com.tave.alarmissue.report.repository;

import com.tave.alarmissue.post.domain.Post;
import com.tave.alarmissue.report.domain.Report;
import com.tave.alarmissue.user.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

}
