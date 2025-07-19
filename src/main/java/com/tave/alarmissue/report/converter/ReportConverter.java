package com.tave.alarmissue.report.converter;


import com.tave.alarmissue.news.domain.NewsComment;
import com.tave.alarmissue.post.domain.Post;
import com.tave.alarmissue.post.domain.PostComment;
import com.tave.alarmissue.post.domain.PostReply;
import com.tave.alarmissue.report.domain.Report;
import com.tave.alarmissue.post.domain.enums.TargetType;
import com.tave.alarmissue.report.dto.response.ReportResponse;
import com.tave.alarmissue.user.domain.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class ReportConverter {

    public static ReportResponse toReportResponseDto(Report report) {
        return ReportResponse.builder().
                reportId(report.getReportId()).
                reported(true).
                targetType(report.getTargetType()).
                build();
    }

    public Report toPostReport(UserEntity user, Post post) {
        return Report.builder().
                reported(true).
                targetType(TargetType.POST).
                user(user).
                post(post).
                build();
    }

    public Report toCommentReport(UserEntity user, PostComment postComment) {
        return Report.builder().
                reported(true).
                targetType(TargetType.COMMENT).
                user(user).
                postComment(postComment).
                build();
    }

    public Report toReplyReport(UserEntity user, PostReply postReply) {
        return Report.builder().
                reported(true).
                targetType(TargetType.REPLY).
                user(user).
                postReply(postReply).
                build();
    }
    public Report toNewsCommentReport(UserEntity user, NewsComment newsComment) {
        return Report.builder().
                reported(true).
                targetType(TargetType.COMMENT).
                user(user).
                newsComment(newsComment).
                build();
    }
}
