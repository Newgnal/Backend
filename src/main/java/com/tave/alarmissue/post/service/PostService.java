package com.tave.alarmissue.post.service;

import com.tave.alarmissue.post.domain.Post;
import com.tave.alarmissue.post.dto.PostCreateRequestDto;
import com.tave.alarmissue.post.dto.PostResponseDto;
import com.tave.alarmissue.post.repository.PostRepository;
import com.tave.alarmissue.user.domain.UserEntity;
import com.tave.alarmissue.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
@RequiredArgsConstructor
@Service
@Slf4j
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostResponseDto createPost(PostCreateRequestDto dto, String userId ) {

        if (userId == null) {
            log.warn("게시글 등록 시도 실패: 유저 정보가 null이다.");
            throw new IllegalArgumentException("로그인 정보가 없습니다.");
        }
        try {

            UserEntity user = userRepository.findById(Long.parseLong(userId))
                    .orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다."));

            Post post = Post.builder()
                    .postTitle(dto.getPostTitle())
                    .postContent(dto.getPostContent())
                    .postImage(dto.getPostImage())
                    .postType(dto.getPostType())
                    .user(user)
                    .build();

            Post saved = postRepository.save(post);

            log.info("게시글 저장 성공 - postId: {}", saved.getPostId());
            return new PostResponseDto(saved.getPostId(), "게시글 등록 완료");
        } catch (Exception e) {
            log.error("게시글 저장 중 예외 발생", e);
            throw new RuntimeException("게시글 저장에 실패했습니다.");
        }
    }
}