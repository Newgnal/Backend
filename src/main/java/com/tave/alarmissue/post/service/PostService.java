package com.tave.alarmissue.post.service;

import com.tave.alarmissue.news.domain.enums.Thema;
import com.tave.alarmissue.post.domain.Comment;
import com.tave.alarmissue.post.dto.response.PostDetailResponseDto;
import com.tave.alarmissue.post.repository.CommentRepository;
import com.tave.alarmissue.post.repository.LikeRepository;
import com.tave.alarmissue.post.converter.PostConverter;
import com.tave.alarmissue.post.domain.Post;
import com.tave.alarmissue.post.dto.request.PostCreateRequestDto;
import com.tave.alarmissue.post.dto.request.PostUpdateRequestDto;
import com.tave.alarmissue.post.dto.response.PostResponseDto;
import com.tave.alarmissue.post.exception.PostException;
import com.tave.alarmissue.post.repository.PostRepository;
import com.tave.alarmissue.user.domain.UserEntity;
import com.tave.alarmissue.user.repository.UserRepository;
import com.tave.alarmissue.post.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
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
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;

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

        likeRepository.deleteAllByPost(post);
        commentRepository.deleteAllByPost(post);
        voteRepository.deleteAllByPost(post);
        postRepository.delete(post);

    }

    @Transactional
    public PostDetailResponseDto getPostDetail(Long postId) {

        Post post = postRepository.findById(postId)
                .orElseThrow(()-> new PostException(POST_ID_NOT_FOUND,"postId:"+ postId));


        postRepository.incrementViewCount(postId); //조회수 증가
        List<Comment> comments = commentRepository.findAllByPost(post);
        return PostConverter.toPostDetailResponseDto(post,comments);
        //대댓글 추가예정
    }
    //전체 게시글 조회
    public Page<PostResponseDto> getAllPost(Pageable pageable) {

     Page<Post> posts= postRepository.findAll(pageable);
     return PostConverter.toPostResponseDtos(posts);
    }
    //게시글 조회 조회순
    public Page<PostResponseDto> getHotPost(Pageable pageable) {
        Page<Post> posts= postRepository.findAll(pageable);
        return PostConverter.toPostResponseDtos(posts);
    }
    //테마별 조회
    public Page<PostResponseDto> getPostByThema(Thema thema,Pageable pageable) {
        Page <Post> posts= postRepository.findAllByThema(thema,pageable);
        return PostConverter.toPostResponseDtos(posts);
    }
    //테마별 조회 인기순
    public Page<PostResponseDto> getHotPostByThema(Thema thema,Pageable pageable) {
        Page <Post> posts= postRepository.findAllByThema(thema,pageable);
        return PostConverter.toPostResponseDtos(posts);
    }
}