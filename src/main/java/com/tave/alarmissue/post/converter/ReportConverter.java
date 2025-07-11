package com.tave.alarmissue.post.converter;

import com.tave.alarmissue.post.domain.Comment;
import com.tave.alarmissue.post.domain.Post;
import com.tave.alarmissue.post.domain.Report;
import com.tave.alarmissue.post.dto.request.ReportCreateRequestDto;
import com.tave.alarmissue.post.dto.response.ReportResponseDto;
import com.tave.alarmissue.user.domain.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class ReportConverter {

    public static ReportResponseDto toReportResponseDto(Report report) {
        return ReportResponseDto.builder().
                reportId(report.getReportId()).
                reported(report.isReported()).
                reportType(report.getReportType()).
                build();
    }

    public Report toPostReport(ReportCreateRequestDto dto, UserEntity user, Post post) {
        return Report.builder().
                reported(dto.isReported()).
                reportType(dto.getReportType()).
                user(user).
                post(post).
                build();
    }
    public Report toCommentReport(ReportCreateRequestDto dto, UserEntity user, Post post,Comment comment) {
        return Report.builder().
                reported(dto.isReported()).
                reportType(dto.getReportType()).
                user(user).
                post(post).
                comment(comment).
                build();
    }
}
