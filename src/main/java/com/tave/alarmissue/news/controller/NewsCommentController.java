package com.tave.alarmissue.news.controller;

import com.tave.alarmissue.auth.dto.request.PrincipalUserDetails;
import com.tave.alarmissue.news.domain.News;
import com.tave.alarmissue.news.dto.request.NewsCommentRequestDto;
import com.tave.alarmissue.news.dto.response.NewsCommentLikeResponse;
import com.tave.alarmissue.news.dto.request.NewsCommentUpdateRequest;
import com.tave.alarmissue.news.dto.request.NewsReplyRequest;
import com.tave.alarmissue.news.dto.response.NewsCommentLikeStatusResponse;
import com.tave.alarmissue.news.dto.response.NewsCommentListResponseDto;
import com.tave.alarmissue.news.dto.response.NewsCommentResponseDto;
import com.tave.alarmissue.news.service.NewsCommentLikeService;
import com.tave.alarmissue.news.service.NewsCommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("news/v1/comment")
@Tag(name = "뉴스 댓글 API")
public class NewsCommentController {

    private final NewsCommentService newsCommentService;
    private final NewsCommentLikeService newsCommentLikeService;

    @PostMapping
    @Operation(summary = "댓글 작성", description = "특정 뉴스에 댓글 작성합니다.")
    public ResponseEntity<NewsCommentResponseDto> createComment(@RequestBody NewsCommentRequestDto dto, @AuthenticationPrincipal PrincipalUserDetails principal) {
        Long userId = principal.getUserId();
        NewsCommentResponseDto responseDto = newsCommentService.createComment(dto,userId, dto.getNewsId());
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping("/{newsId}")
    @Operation(summary = "댓글 목록 조회", description = "특정 뉴스의 모든 댓글을 최신순으로 조회합니다.")
    public ResponseEntity<NewsCommentListResponseDto> getComments(@PathVariable Long newsId, @AuthenticationPrincipal PrincipalUserDetails principal) {

        Long userId = null;
        if (principal != null) {
            userId = principal.getUserId();
        }

        NewsCommentListResponseDto comments=newsCommentService.getCommentsByNewsId(newsId,userId);
        return ResponseEntity.ok(comments);
    }

    @DeleteMapping("/{commentId}")
    @Operation(summary = "댓글/답글 삭제", description = "특정 댓글을 삭제합니다. (작성자만 가능), (답글도 모두 삭제)")
    public ResponseEntity<Void>deleteComment(@PathVariable Long commentId,
                                             @AuthenticationPrincipal PrincipalUserDetails principal){
        Long userId=principal.getUserId();
        newsCommentService.deleteComment(commentId,userId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }

    @PatchMapping("/{commentId}")
    @Operation(summary = "댓글/답글 수정", description = "특정 댓글을 수정합니다. (작성자만 가능)")
    public ResponseEntity<NewsCommentResponseDto> updateComment(@RequestBody NewsCommentUpdateRequest dto,
                                                               @PathVariable Long commentId,
                                                               @AuthenticationPrincipal PrincipalUserDetails principal){
        Long userId = principal.getUserId();
        NewsCommentResponseDto response = newsCommentService.updateComment(commentId, userId, dto);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{commentId}")
    @Operation(summary = "답글 생성", description = "특정 댓글에 대댓글을 작성합니다.")
    public ResponseEntity<NewsCommentResponseDto> createReply(@RequestBody NewsReplyRequest dto,

                                                              @AuthenticationPrincipal PrincipalUserDetails principal ){
        Long userId=principal.getUserId();


        NewsCommentResponseDto response=newsCommentService.createReply(userId,dto);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/like/{commentId}")
    @Operation(summary = "댓글/답글 좋아요", description = "댓글/답글에 좋아요를 추가하거나 제거합니다.")
    public ResponseEntity<NewsCommentLikeResponse> toggleLike(@PathVariable Long commentId, @AuthenticationPrincipal PrincipalUserDetails principal){
        Long userId= principal.getUserId();
        NewsCommentLikeResponse response=newsCommentLikeService.commentLike(commentId,userId);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/like/{commentId}/status")
    @Operation(summary = "댓글 좋아요 상태 조회", description = "특정 사용자의 댓글 좋아요 상태를 조회합니다.")
    public ResponseEntity<NewsCommentLikeStatusResponse> getLikeStatus(
            @PathVariable Long commentId,
            @AuthenticationPrincipal PrincipalUserDetails principal) {

        Long userId = principal.getUserId();
        NewsCommentLikeStatusResponse response = newsCommentLikeService.getLikeStatus(commentId, userId);

        return ResponseEntity.ok(response);
    }

}
