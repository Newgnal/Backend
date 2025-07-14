package com.tave.alarmissue.inquiry.repository;

import com.tave.alarmissue.inquiry.domain.Inquiry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InquiryRepository extends JpaRepository<Inquiry, Long> {
}
