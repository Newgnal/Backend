package com.tave.alarmissue.post.service;

import com.tave.alarmissue.post.domain.PostEntity;
import com.tave.alarmissue.post.dto.PostCreateRequestDto;
import com.tave.alarmissue.post.dto.PostResponseDto;
import com.tave.alarmissue.post.repository.PostRepository;
import com.tave.alarmissue.user.domain.UserEntity;
import com.tave.alarmissue.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@RequiredArgsConstructor
@Service
public class PostService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public PostResponseDto createPost(PostCreateRequestDto dto, UserEntity user)
    {
        UserEntity users = userRepository.findById(2L)
                .orElseThrow(() -> new RuntimeException("테스트용 유저(id=1) 없음"));

        PostEntity postEntity = PostEntity.builder()
                .postTitle(dto.getPostTitle())
                .postContent(dto.getPostContent())
                .postImage(dto.getPostImage())
                .postType(dto.getPostType())
                .user(users)
                .build();

        PostEntity saved = postRepository.save(postEntity);
        return new PostResponseDto(saved.getPostId(), "게시글 등록 완료");
    }
}
