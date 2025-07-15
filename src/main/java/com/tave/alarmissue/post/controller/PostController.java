package com.tave.alarmissue.post.controller;

import com.tave.alarmissue.auth.dto.request.PrincipalUserDetails;
import com.tave.alarmissue.news.domain.enums.Thema;
import com.tave.alarmissue.post.converter.PostConverter;
import com.tave.alarmissue.post.domain.enums.SortType;
import com.tave.alarmissue.post.dto.request.PostCreateRequest;

import com.tave.alarmissue.post.dto.request.PostUpdateRequest;

import com.tave.alarmissue.post.dto.response.PageResponse;
import com.tave.alarmissue.post.dto.response.PostDetailResponse;
import com.tave.alarmissue.post.dto.response.PostHomeResponse;
import com.tave.alarmissue.post.dto.response.PostResponse;

import com.tave.alarmissue.post.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.function.Function;


@RequiredArgsConstructor
@RestController
@RequestMapping("/post/v1")
@Tag(name = "커뮤니티 게시글 API")
public class PostController {

    private final PostService postService;

    //게시글 작성
    @PostMapping
    @Operation(summary = "게시글 작성", description = "게시글 작성 입니다.")
    public ResponseEntity<PostResponse> createPost(@RequestBody PostCreateRequest dto,
                                                   @AuthenticationPrincipal PrincipalUserDetails principal) {
        Long userId = principal.getUserId();

        PostResponse responseDto = postService.createPost(dto, userId);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);

    }

    //게시글 수정
    @PatchMapping("/{postId}")
    @Operation(summary = "게시글 수정", description = "해당 게시글을 수정합니다.")
    public ResponseEntity<PostResponse> updatePost(@PathVariable Long postId,
                                                   @RequestBody PostUpdateRequest dto,
                                                   @AuthenticationPrincipal PrincipalUserDetails principal) {
        Long userId = principal.getUserId();
        PostResponse responseDto = postService.updatePost(postId, dto, userId);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);

    }

    //게시글 삭제
    @DeleteMapping("/{postId}")
    @Operation(summary = "게시글 삭제", description = "해당 게시글을 삭제합니다.")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId,
                                           @AuthenticationPrincipal PrincipalUserDetails principal) {
        Long userId = principal.getUserId();
        postService.deletePost(postId, userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    //게시글 상세조회
    @GetMapping("/detail/{postId}")
    @Operation(summary = "게시글 상세조회", description = "해당 게시글의 내용,투표,댓글 조회입니다.")
    public ResponseEntity<PostDetailResponse> getPostDetail(@PathVariable Long postId,
                                                            @AuthenticationPrincipal PrincipalUserDetails principal) {

        Long userId = principal != null ? principal.getUserId() : null;
        postService.incrementViewCountAsync(postId);

        PostDetailResponse responseDto = postService.getPostDetail(postId,userId);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);

    }

}


