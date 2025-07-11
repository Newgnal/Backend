package com.tave.alarmissue.post.controller;

import com.tave.alarmissue.auth.dto.request.PrincipalUserDetails;
import com.tave.alarmissue.post.dto.request.PostCreateRequestDto;
import com.tave.alarmissue.post.dto.request.PostUpdateRequestDto;
import com.tave.alarmissue.post.dto.response.PostDetailResponseDto;
import com.tave.alarmissue.post.dto.response.PostResponseDto;
import com.tave.alarmissue.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/posts/v1")
public class PostController {

    private final PostService postService;
    //게시글 작성
    @PostMapping
    public ResponseEntity<PostResponseDto> createPost(@RequestBody PostCreateRequestDto dto, @AuthenticationPrincipal PrincipalUserDetails principal)

    {
        Long userId = principal.getUserId();

        PostResponseDto responseDto = postService.createPost(dto, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);

    }
    //게시글 수정
    @PatchMapping("/{postId}")
    public ResponseEntity<PostResponseDto> updatePost(@PathVariable Long postId ,@RequestBody PostUpdateRequestDto dto , @AuthenticationPrincipal PrincipalUserDetails principal)
    {
        Long userId = principal.getUserId();
        PostResponseDto responseDto = postService.updatePost(postId, dto, userId);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);

    }

    //게시글 삭제
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId, @AuthenticationPrincipal PrincipalUserDetails principal){
        Long userId = principal.getUserId();
        postService.deletePost(postId, userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    //게시글 상세조회
    @GetMapping("/{postId}")
    public ResponseEntity<PostDetailResponseDto> getPostDetail(@PathVariable Long postId){
        PostDetailResponseDto responseDto = postService.getPostDetail(postId);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);

    }
    //게시글 전체 조회(최신순)
    @GetMapping
    public ResponseEntity<Page<PostResponseDto>> getAllPost(
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        Page<PostResponseDto> responseDto = postService.getAllPost(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
    //게시글 전체 조회(인기순=조회수순)
    @GetMapping("hot")
    public ResponseEntity<Page<PostResponseDto>> getHotPost(
            @PageableDefault(size = 10, sort = "viewCount", direction = Sort.Direction.DESC)
            Pageable pageable)
    {
        Page<PostResponseDto> responseDto =  postService.getHotPost(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
    //게시글 테마별 조회(최신순)
    //게시글 테마별 조회(인기순=조회수순)
    //홈화면 조회

}
