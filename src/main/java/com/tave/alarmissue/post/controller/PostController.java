package com.tave.alarmissue.post.controller;

import com.tave.alarmissue.auth.dto.request.PrincipalUserDetails;
import com.tave.alarmissue.news.domain.enums.Thema;
import com.tave.alarmissue.post.dto.request.PostCreateRequest;

import com.tave.alarmissue.post.dto.request.PostUpdateRequest;

import com.tave.alarmissue.post.dto.response.PostDetailResponse;
import com.tave.alarmissue.post.dto.response.PostHomeResponse;
import com.tave.alarmissue.post.dto.response.PostResponse;

import com.tave.alarmissue.post.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("/post/v1")
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
    @GetMapping("/{postId}")
    @Operation(summary = "게시글 상세조회", description = "해당 게시글의 내용,투표,댓글 조회입니다.")
    public ResponseEntity<PostDetailResponse> getPostDetail(@PathVariable Long postId,
                                                            @AuthenticationPrincipal PrincipalUserDetails principal) {
        Long userId = principal.getUserId();
        PostDetailResponse responseDto = postService.getPostDetail(postId,userId);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);

    }

    //게시글 전체 조회(최신순)
    @GetMapping
    @Operation(summary = "게시글 전체조회(최신순)", description = "게시글을 전체 조회합니다(최신순)")
    public ResponseEntity<Page<PostResponse>> getAllPost(
            @ParameterObject
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        Page<PostResponse> responseDto = postService.getAllPost(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    //게시글 전체 조회(인기순=조회수순)
    @GetMapping("/hot")
    @Operation(summary = "게시글 전체조회(인기순)", description = "게시글을 전체 조회합니다(조회수순)")
    public ResponseEntity<Page<PostResponse>> getHotPost(
            @ParameterObject
            @PageableDefault(size = 10, sort = "viewCount", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        Page<PostResponse> responseDto = postService.getHotPost(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    //게시글 테마별 조회(최신순)
    @GetMapping("/thema/{thema}")
    @Operation(summary = "게시글 테마 별 조회", description = "해당 테마의 게시글들을 조회합니다(최신순)")
    public ResponseEntity<Page<PostResponse>> getPostByThema(
            @PathVariable Thema thema,
            @ParameterObject
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        Page<PostResponse> responseDto = postService.getPostByThema(thema, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    //게시글 테마별 조회(인기순=조회수순)
    @GetMapping("/thema/hot/{thema}")
    @Operation(summary = "게시글 테마 별 조회", description = "해당 테마의 게시글들을 조회합니다(조회수순)")
    public ResponseEntity<Page<PostResponse>> getHotPostByThema(
            @PathVariable Thema thema,
            @ParameterObject
            @PageableDefault(size = 10, sort = "viewCount", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        Page<PostResponse> responseDto = postService.getHotPostByThema(thema, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @GetMapping("/home")
    @Operation(summary = "게시글 홈 화면 조회", description = "인기 테마3개(게시글 순), 인기글 9개(조회수순), 최신 글 4개를 조회합니다")
    public ResponseEntity<PostHomeResponse> getPostHome(){
        PostHomeResponse responseDto = postService.getPostHome();
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}


