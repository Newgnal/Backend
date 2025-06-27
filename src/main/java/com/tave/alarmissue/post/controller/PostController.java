package com.tave.alarmissue.post.controller;

import com.tave.alarmissue.auth.dto.request.PrincipalUserDetails;
import com.tave.alarmissue.post.dto.request.PostCreateRequestDto;
import com.tave.alarmissue.post.dto.request.PostUpdateRequestDto;
import com.tave.alarmissue.post.dto.response.PostResponseDto;
import com.tave.alarmissue.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

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
    public ResponseEntity<PostResponseDto> deletePost(@PathVariable Long postId, @AuthenticationPrincipal PrincipalUserDetails principal){
        Long userId = principal.getUserId();
        PostResponseDto responseDto = postService.deletePost(postId, userId);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

}
