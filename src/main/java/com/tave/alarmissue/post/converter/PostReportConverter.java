package com.tave.alarmissue.post.converter;


import com.tave.alarmissue.post.domain.Post;
import com.tave.alarmissue.post.domain.PostComment;
import com.tave.alarmissue.post.domain.PostReply;
import com.tave.alarmissue.post.domain.PostReport;
import com.tave.alarmissue.post.domain.enums.TargetType;
import com.tave.alarmissue.post.dto.response.ReportResponse;
import com.tave.alarmissue.user.domain.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class PostReportConverter {

    public static ReportResponse toReportResponseDto(PostReport postReport) {
        return ReportResponse.builder().
                reportId(postReport.getReportId()).
                reported(true).
                targetType(postReport.getTargetType()).
                build();
    }

    public PostReport toPostReport(UserEntity user, Post post) {
        return PostReport.builder().
                reported(true).
                targetType(TargetType.POST).
                user(user).
                post(post).
                build();
    }

    public PostReport toCommentReport( UserEntity user, PostComment postComment) {
        return PostReport.builder().
                reported(true).
                targetType(TargetType.COMMENT).
                user(user).
                postComment(postComment).
                build();
    }

    public PostReport toReplyReport(UserEntity user, PostReply postReply) {
        return PostReport.builder().
                reported(true).
                targetType(TargetType.REPLY).
                user(user).
                postReply(postReply).
                build();
    }
}
