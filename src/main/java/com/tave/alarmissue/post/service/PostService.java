package com.tave.alarmissue.post.service;

import com.tave.alarmissue.post.converter.PostConverter;
import com.tave.alarmissue.post.domain.Post;
import com.tave.alarmissue.post.dto.request.PostCreateRequestDto;
import com.tave.alarmissue.post.dto.request.PostUpdateRequestDto;
import com.tave.alarmissue.post.dto.response.PostResponseDto;
import com.tave.alarmissue.post.exception.PostException;
import com.tave.alarmissue.post.repository.PostRepository;
import com.tave.alarmissue.user.domain.UserEntity;
import com.tave.alarmissue.user.repository.UserRepository;
import com.tave.alarmissue.vote.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

import static com.tave.alarmissue.post.exception.PostErrorCode.*;

@RequiredArgsConstructor
@Service
@Slf4j
@Transactional(readOnly = true)
public class PostService {
    private final PostConverter postConverter;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final VoteRepository voteRepository;


    @Transactional
    public PostResponseDto createPost(PostCreateRequestDto dto, Long userId) {

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new PostException(USER_ID_NOT_FOUND, "해당 유저를 찾을 수 없습니다."));

        Post post = postConverter.toPost(dto, user);
        Post saved = postRepository.save(post);

        return PostConverter.toPostResponseDto(saved);

    }

    //게시글 수정

    @Transactional
    public PostResponseDto updatePost(Long postId, PostUpdateRequestDto dto, Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new PostException(USER_ID_NOT_FOUND, "해당 유저를 찾을 수 없습니다."));

        Post post = postRepository.findById(postId)
                .orElseThrow(()-> new PostException(POST_ID_NOT_FOUND,"postId:"+ postId));

        if(!Objects.equals(post.getUser().getId(), user.getId())) {
            throw new PostException(POST_EDIT_FORBIDDEN,"post의 userId: "+ post.getUser().getId() + " userId: "+ user.getId());
        }

        post.Update(dto.getPostTitle(),dto.getPostContent(),dto.getArticleUrl(),dto.getThema(),dto.isHasVote());

        //투표기능끄면 post와 연관된 vote DB삭제
        if(!post.getHasVote()) voteRepository.deleteAllByPost(post);
        Post saved = postRepository.save(post);
        return PostConverter.toPostResponseDto(saved);

    }
    //게시글 삭제
    @Transactional
    public void deletePost(Long postId, Long userId){

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new PostException(USER_ID_NOT_FOUND, "해당 유저를 찾을 수 없습니다."));

        Post post = postRepository.findById(postId)
                .orElseThrow(()-> new PostException(POST_ID_NOT_FOUND,"postId:"+ postId));

        if(!Objects.equals(post.getUser().getId(), user.getId())) {
            throw new PostException(POST_DELETE_FORBIDDEN,"post의 userId: "+ post.getUser().getId() + " userId: "+ user.getId());
        }

        voteRepository.deleteAllByPost(post);
        postRepository.delete(post);

    }


}