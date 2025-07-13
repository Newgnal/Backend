package com.tave.alarmissue.post.converter;


import com.tave.alarmissue.post.domain.Post;
import com.tave.alarmissue.post.domain.PostComment;
import com.tave.alarmissue.post.domain.PostReply;
import com.tave.alarmissue.post.domain.PostReport;
import com.tave.alarmissue.post.dto.request.ReportCreateRequestDto;
import com.tave.alarmissue.post.dto.response.ReportResponse;
import com.tave.alarmissue.user.domain.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class PostReportConverter {

    public static ReportResponse toReportResponseDto(PostReport postReport) {
        return ReportResponse.builder().
                reportId(postReport.getReportId()).
                reported(postReport.isReported()).
                reportType(postReport.getReportType()).
                build();
    }

    public PostReport toPostReport(ReportCreateRequestDto dto, UserEntity user, Post post) {
        return PostReport.builder().
                reported(dto.isReported()).
                reportType(dto.getReportType()).
                user(user).
                post(post).
                build();
    }

    public PostReport toCommentReport(ReportCreateRequestDto dto, UserEntity user, PostComment postComment) {
        return PostReport.builder().
                reported(dto.isReported()).
                reportType(dto.getReportType()).
                user(user).
                postComment(postComment).
                build();
    }

    public PostReport toReplyReport(ReportCreateRequestDto dto, UserEntity user, PostReply postReply) {
        return PostReport.builder().
                reported(dto.isReported()).
                reportType(dto.getReportType()).
                user(user).
                postReply(postReply).
                build();
    }
}
