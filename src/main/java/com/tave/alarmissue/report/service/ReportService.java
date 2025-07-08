package com.tave.alarmissue.report.service;

import com.tave.alarmissue.comment.domain.Comment;
import com.tave.alarmissue.comment.repository.CommentRepository;
import com.tave.alarmissue.post.domain.Post;
import com.tave.alarmissue.post.repository.PostRepository;
import com.tave.alarmissue.report.converter.ReportConverter;
import com.tave.alarmissue.report.domain.Report;
import com.tave.alarmissue.report.dto.requestdto.ReportCreateRequestDto;
import com.tave.alarmissue.report.dto.responsedto.ReportResponseDto;
import com.tave.alarmissue.report.exception.ReportException;
import com.tave.alarmissue.report.repository.ReportRepository;
import com.tave.alarmissue.user.domain.UserEntity;
import com.tave.alarmissue.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Objects;

import static com.tave.alarmissue.report.exception.ReportErrorCode.*;

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
                orElseThrow(() -> new ReportException(USER_ID_NOT_FOUND,"유저가 없습니다."));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ReportException(POST_ID_NOT_FOUND," postId: " + postId));

        //게시글 신고자가 본인 일 경우
        if(Objects.equals(user.getId(), post.getUser().getId())) {
            throw new ReportException(POST_REPORT_FORBIDDEN," userId: " + user.getId()+" post의 userId: "+ post.getUser().getId());
        }
       Report report = reportConverter.toPostReport(dto,user,post);
        Report saved = reportRepository.save(report);
        return ReportConverter.toReportResponseDto(saved);
    }

    public ReportResponseDto createCommentReport(ReportCreateRequestDto dto, Long userId, Long postId, Long commentId) {

        UserEntity user = userRepository.findById(userId).
                orElseThrow(() -> new ReportException(USER_ID_NOT_FOUND,"유저가 없습니다."));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ReportException(POST_ID_NOT_FOUND," postId: " + postId));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(()->new ReportException(COMMENT_ID_NOT_FOUND,"commentId:"+commentId));

        //게시글의 id와 댓글의 postId가 다를 경우
        if(!comment.getPost().getPostId().equals(post.getPostId())){
            throw new ReportException(POST_ID_MIXMATCH,"postId: "+ post.getPostId() +"comment의 postId: "+ comment.getPost().getPostId());
        }
        //신고자가 본인 일 경우
        if(Objects.equals(user.getId(), comment.getUser().getId())) {
            throw new ReportException(COMMENT_REPORT_FORBIDDEN," userId: " + user.getId()+" comment 의 userId: "+ comment.getUser().getId());
        }
        Report report = reportConverter.toCommentReport(dto,user,post,comment);
        Report saved = reportRepository.save(report);
        return ReportConverter.toReportResponseDto(saved);
    }
}
