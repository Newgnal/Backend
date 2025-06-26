package com.tave.alarmissue.post.service;

import com.tave.alarmissue.post.converter.PostConverter;
import com.tave.alarmissue.post.domain.Post;
import com.tave.alarmissue.post.dto.request.PostCreateRequestDto;
import com.tave.alarmissue.post.dto.response.PostResponseDto;
import com.tave.alarmissue.post.exception.PostException;
import com.tave.alarmissue.post.repository.PostRepository;
import com.tave.alarmissue.user.domain.UserEntity;
import com.tave.alarmissue.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.tave.alarmissue.post.exception.PostErrorCode.*;

@RequiredArgsConstructor
@Service
@Slf4j
public class PostService {
    private final PostConverter postConverter;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostResponseDto createPost(PostCreateRequestDto dto, Long userId) {

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new PostException(USER_ID_NOT_FOUND, "해당 유저를 찾을 수 없습니다."));

        Post post = postConverter.toPost(dto, user);
        Post saved = postRepository.save(post);

        return PostConverter.toPostResponseDto(saved);

    }
}