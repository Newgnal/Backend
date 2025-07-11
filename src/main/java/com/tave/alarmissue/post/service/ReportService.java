package com.tave.alarmissue.post.service;

import com.tave.alarmissue.post.domain.Comment;
import com.tave.alarmissue.post.repository.CommentRepository;
import com.tave.alarmissue.post.domain.Post;
import com.tave.alarmissue.post.repository.PostRepository;
import com.tave.alarmissue.post.converter.ReportConverter;
import com.tave.alarmissue.post.domain.Report;
import com.tave.alarmissue.post.dto.request.ReportCreateRequestDto;
import com.tave.alarmissue.post.dto.response.ReportResponseDto;
import com.tave.alarmissue.post.exception.ReportException;
import com.tave.alarmissue.post.repository.ReportRepository;
import com.tave.alarmissue.user.domain.UserEntity;
import com.tave.alarmissue.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

import static com.tave.alarmissue.post.exception.ReportErrorCode.*;

@RequiredArgsConstructor
@Service
@Slf4j
@Transactional(readOnly = true)
public class ReportService {
    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final ReportConverter reportConverter;
    private final CommentRepository commentRepository;

    @Transactional
    public ReportResponseDto createPostReport(ReportCreateRequestDto dto, Long userId, Long postId) {

        UserEntity user = userRepository.findById(userId).
                orElseThrow(() -> new ReportException(USER_ID_NOT_FOUND, "유저가 없습니다."));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ReportException(POST_ID_NOT_FOUND, " postId: " + postId));

        //게시글 신고자가 본인 일 경우
        if (Objects.equals(user.getId(), post.getUser().getId())) {
            throw new ReportException(POST_REPORT_FORBIDDEN, " userId: " + user.getId() + " post의 userId: " + post.getUser().getId());
        }

        //이미 신고가 있을경우
        Optional<Report> existingReport = reportRepository.findPostReport(user, post);
        if(existingReport.isPresent()) {
            throw new ReportException(ALREADY_POST_REPORTED);
        }
        Report report = reportConverter.toPostReport(dto, user, post);
        Report saved = reportRepository.save(report);
        return ReportConverter.toReportResponseDto(saved);
    }

    @Transactional
    public ReportResponseDto createCommentReport(ReportCreateRequestDto dto, Long userId,Long commentId) {

        UserEntity user = userRepository.findById(userId).
                orElseThrow(() -> new ReportException(USER_ID_NOT_FOUND, "유저가 없습니다."));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ReportException(COMMENT_ID_NOT_FOUND, "commentId:" + commentId));

        Post post = postRepository.findById(comment.getPost().getPostId())
                .orElseThrow(() -> new ReportException(POST_ID_NOT_FOUND, " postId: " + comment.getPost().getPostId()));

        //게시글의 id와 댓글의 postId가 다를 경우
        if (!comment.getPost().getPostId().equals(post.getPostId())) {
            throw new ReportException(POST_ID_MIXMATCH, "postId: " + post.getPostId() + "comment의 postId: " + comment.getPost().getPostId());
        }
        //신고자가 본인 일 경우
        if (Objects.equals(user.getId(), comment.getUser().getId())) {
            throw new ReportException(COMMENT_REPORT_FORBIDDEN, " userId: " + user.getId() + " comment 의 userId: " + comment.getUser().getId());
        }
        //신고가 있는경우
        Optional<Report> existingReport = reportRepository.findCommentReport(user, comment);
        if(existingReport.isPresent()) {
            throw new ReportException(ALREADY_POST_REPORTED);
        }

        Report report = reportConverter.toCommentReport(dto, user, post, comment);
        Report saved = reportRepository.save(report);
        return ReportConverter.toReportResponseDto(saved);
    }
}
