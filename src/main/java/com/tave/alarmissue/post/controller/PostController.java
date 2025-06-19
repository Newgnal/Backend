package com.tave.alarmissue.post.controller;

import com.tave.alarmissue.post.domain.PostEntity;
import com.tave.alarmissue.post.domain.PostType;
import com.tave.alarmissue.post.dto.PostCreateRequestDto;
import com.tave.alarmissue.post.dto.PostResponseDto;
import com.tave.alarmissue.post.service.PostService;
import com.tave.alarmissue.user.domain.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostResponseDto> createPost(@RequestBody PostCreateRequestDto dto, @AuthenticationPrincipal UserEntity user) {
        PostResponseDto responseDto = postService.createPost(dto, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);




    }



}
