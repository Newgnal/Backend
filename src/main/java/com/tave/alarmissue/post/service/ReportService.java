package com.tave.alarmissue.post.service;

import com.tave.alarmissue.post.domain.PostComment;
import com.tave.alarmissue.post.domain.PostReply;
import com.tave.alarmissue.post.domain.PostReport;
import com.tave.alarmissue.post.exception.PostException;
import com.tave.alarmissue.post.repository.CommentRepository;
import com.tave.alarmissue.post.domain.Post;
import com.tave.alarmissue.post.repository.PostRepository;
import com.tave.alarmissue.post.converter.PostReportConverter;
import com.tave.alarmissue.post.dto.response.ReportResponse;
import com.tave.alarmissue.post.repository.ReplyRepository;
import com.tave.alarmissue.post.repository.ReportRepository;
import com.tave.alarmissue.user.domain.UserEntity;
import com.tave.alarmissue.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

import static com.tave.alarmissue.post.exception.PostErrorCode.*;
import static com.tave.alarmissue.post.exception.PostErrorCode.REPLY_ID_NOT_FOUND;


@RequiredArgsConstructor
@Service
@Slf4j
@Transactional(readOnly = true)
public class ReportService {
    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final PostReportConverter postReportConverter;
    private final CommentRepository commentRepository;
    private final ReplyRepository replyRepository;

    @Transactional
    public ReportResponse createPostReport(Long userId, Long postId) {

        UserEntity user = getUserById(userId);
        Post post = getPostById(postId);

        validateNotSelfReport(user.getId(), post.getUser().getId());

        //이미 신고가 있을경우
        boolean alreadyReported = reportRepository.findPostReport(user, post).
                isPresent();
        validateNotAlreadyReported(alreadyReported);

            PostReport report = postReportConverter.toPostReport(user, post);
            PostReport saved = reportRepository.save(report);
            return PostReportConverter.toReportResponseDto(saved);

    }
    //
    @Transactional
    public ReportResponse createCommentReport(Long userId, Long commentId) {

        UserEntity user = getUserById(userId);
        PostComment postComment = getPostCommentById(commentId);

        validateNotSelfReport(user.getId(), postComment.getUser().getId());

        //신고가 있는경우
        boolean alreadyReported = reportRepository.findCommentReport(user, postComment).
                isPresent();
        validateNotAlreadyReported(alreadyReported);

            PostReport report = postReportConverter.toCommentReport(user, postComment);
            PostReport saved = reportRepository.save(report);
            return PostReportConverter.toReportResponseDto(saved);

    }
    @Transactional
    public ReportResponse createReplyReport(Long userId, Long replyId) {
        UserEntity user = getUserById(userId);
        PostReply postReply = getPostReplyById(replyId);

        validateNotSelfReport(user.getId(), postReply.getUser().getId());

        boolean alreadyReported = reportRepository.findReplyReport(user, postReply)
                .isPresent();
        validateNotAlreadyReported(alreadyReported);

            PostReport report = postReportConverter.toReplyReport(user, postReply);
            PostReport saved = reportRepository.save(report);
            return PostReportConverter.toReportResponseDto(saved);

    }

    /*
      private method 분리
       */
    private UserEntity getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new PostException(USER_ID_NOT_FOUND, "userId"+userId));
    }

    private Post getPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new PostException(POST_ID_NOT_FOUND," postId: " + postId));
    }

    private PostComment getPostCommentById(Long commentId) {

        return commentRepository.findById(commentId).
                orElseThrow(() -> new PostException(COMMENT_ID_NOT_FOUND, " commentId: " + commentId));
    }

    private PostReply getPostReplyById(Long replyId){
        return replyRepository.findById(replyId).
                orElseThrow(() -> new PostException(REPLY_ID_NOT_FOUND," replyId: " + replyId));
    }
    //이미 신고되었을때
    private void validateNotAlreadyReported(boolean alreadyReported) {
        if (alreadyReported) {
            throw new PostException(ALREADY_REPORTED);
        }
    }
    //신고자가 본인일때
    private void validateNotSelfReport(Long reporterId, Long targetOwnerId) {
        if (Objects.equals(reporterId, targetOwnerId)) {
            throw new PostException(CANNOT_REPORT," userId: " + reporterId);
        }
    }
}
