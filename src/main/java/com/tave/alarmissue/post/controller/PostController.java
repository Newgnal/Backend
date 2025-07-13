package com.tave.alarmissue.post.controller;

import com.tave.alarmissue.auth.dto.request.PrincipalUserDetails;
import com.tave.alarmissue.news.domain.enums.Thema;
import com.tave.alarmissue.post.dto.request.PostCreateRequest;

import com.tave.alarmissue.post.dto.request.PostUpdateRequest;

import com.tave.alarmissue.post.dto.response.PostDetailResponse;
import com.tave.alarmissue.post.dto.response.PostResponse;

import com.tave.alarmissue.post.service.PostService;
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
@RequestMapping("/posts/v1")
public class PostController {

    private final PostService postService;

    //게시글 작성
    @PostMapping
    public ResponseEntity<PostResponse> createPost(@RequestBody PostCreateRequest dto,
                                                   @AuthenticationPrincipal PrincipalUserDetails principal) {
        Long userId = principal.getUserId();

        PostResponse responseDto = postService.createPost(dto, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);

    }

    //게시글 수정
    @PatchMapping("/{postId}")
    public ResponseEntity<PostResponse> updatePost(@PathVariable Long postId,
                                                   @RequestBody PostUpdateRequest dto,
                                                   @AuthenticationPrincipal PrincipalUserDetails principal) {
        Long userId = principal.getUserId();
        PostResponse responseDto = postService.updatePost(postId, dto, userId);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);

    }

    //게시글 삭제
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId,
                                           @AuthenticationPrincipal PrincipalUserDetails principal) {
        Long userId = principal.getUserId();
        postService.deletePost(postId, userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    //게시글 상세조회
    @GetMapping("/{postId}")
    public ResponseEntity<PostDetailResponse> getPostDetail(@PathVariable Long postId,
                                                            @AuthenticationPrincipal PrincipalUserDetails principal) {
        Long userId = principal.getUserId();
        PostDetailResponse responseDto = postService.getPostDetail(postId,userId);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);

    }

    //게시글 전체 조회(최신순)
    @GetMapping
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
    public ResponseEntity<Page<PostResponse>> getHotPost(
            @ParameterObject
            @PageableDefault(size = 10, sort = "viewCount", direction = Sort.Direction.DESC)
            Pageable pageable) {
        Page<PostResponse> responseDto = postService.getHotPost(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    //게시글 테마별 조회(최신순)
    @GetMapping("/thema/{thema}")
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
    public ResponseEntity<Page<PostResponse>> getHotPostByThema(
            @PathVariable Thema thema,
            @ParameterObject
            @PageableDefault(size = 10, sort = "viewCount", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        Page<PostResponse> responseDto = postService.getHotPostByThema(thema, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}
//    //홈화면 조회
//    @GetMapping("/home")
//    public ResponseEntity
//}
