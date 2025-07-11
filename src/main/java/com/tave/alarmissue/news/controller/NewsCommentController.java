package com.tave.alarmissue.news.controller;

import com.tave.alarmissue.auth.dto.request.PrincipalUserDetails;
import com.tave.alarmissue.news.dto.request.NewsCommentCreateRequestDto;
import com.tave.alarmissue.news.dto.response.NewsCommentListResponseDto;
import com.tave.alarmissue.news.dto.response.NewsCommentResponseDto;
import com.tave.alarmissue.news.service.NewsCommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/news/v1/{newsId}/comments")
public class NewsCommentController {

    private final NewsCommentService newsCommentService;

//    @PostMapping
//    @Operation(summary = "댓글 작성", description = "특정 뉴스에 댓글 작성합니다.")
//    public ResponseEntity<NewsCommentCreateResponseDto> createComment(@RequestBody NewsCommentCreateRequestDto dto, @AuthenticationPrincipal PrincipalUserDetails principal) {
//        Long userId = principal.getUserId();
//        NewsCommentCreateResponseDto responseDto = newsCommentService.createComment(dto,userId, dto.getNewsId());
//        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
//    }
//
//    @GetMapping
//    @Operation(summary = "댓글 목록 조회", description = "특정 뉴스의 모든 댓글을 최신순으로 조회합니다.")
//    public ResponseEntity<List<NewsCommentResponseDto>> getComments(@PathVariable Long newsId){
//        List<NewsCommentResponseDto> comments=newsCommentService.getCommentsByNewsId(newsId);
//        return ResponseEntity.ok(comments);
//    }

    @PostMapping
    @Operation(summary = "댓글 작성", description = "특정 뉴스에 댓글 작성합니다.")
    public ResponseEntity<NewsCommentResponseDto> createComment(@RequestBody NewsCommentCreateRequestDto dto, @AuthenticationPrincipal PrincipalUserDetails principal) {
        Long userId = principal.getUserId();
        NewsCommentResponseDto responseDto = newsCommentService.createComment(dto,userId, dto.getNewsId());
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping
    @Operation(summary = "댓글 목록 조회", description = "특정 뉴스의 모든 댓글을 최신순으로 조회합니다.")
    public ResponseEntity<NewsCommentListResponseDto> getComments(@PathVariable Long newsId){
        NewsCommentListResponseDto response=newsCommentService.getCommentsByNewsId(newsId);
        return ResponseEntity.ok(response);
    }

}
