package com.tave.alarmissue.post.controller;

import com.tave.alarmissue.auth.dto.request.PrincipalUserDetails;
import com.tave.alarmissue.post.dto.request.PostCreateRequest;
import com.tave.alarmissue.post.dto.request.PostUpdateRequest;
import com.tave.alarmissue.post.dto.response.PostResponse;
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
@RequestMapping("/post/v1")
public class PostController {

    private final PostService postService;
    //게시글 작성
    @PostMapping
    public ResponseEntity<PostResponse> createPost(@RequestBody PostCreateRequest dto,
                                                   @AuthenticationPrincipal PrincipalUserDetails principal)
    {
        Long userId = principal.getUserId();

        PostResponse responseDto = postService.createPost(dto, userId);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);

    }

    //게시글 수정
    @PatchMapping("/{postId}")
    public ResponseEntity<PostResponse> updatePost(@PathVariable Long postId ,
                                                   @RequestBody PostUpdateRequest dto ,
                                                   @AuthenticationPrincipal PrincipalUserDetails principal)
    {
        Long userId = principal.getUserId();
        PostResponse responseDto = postService.updatePost(postId, dto, userId);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);

    }

    //게시글 삭제
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId,
                                           @AuthenticationPrincipal PrincipalUserDetails principal){
        Long userId = principal.getUserId();

        postService.deletePost(postId, userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
