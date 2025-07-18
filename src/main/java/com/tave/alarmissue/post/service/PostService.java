package com.tave.alarmissue.post.service;

import com.tave.alarmissue.news.domain.News;
import com.tave.alarmissue.news.domain.enums.Thema;
import com.tave.alarmissue.news.repository.NewsRepository;
import com.tave.alarmissue.post.converter.PostCommentConverter;
import com.tave.alarmissue.post.converter.PostVoteConverter;
import com.tave.alarmissue.post.domain.PostComment;
import com.tave.alarmissue.post.domain.PostReply;
import com.tave.alarmissue.post.domain.PostVote;
import com.tave.alarmissue.post.domain.enums.VoteType;
import com.tave.alarmissue.post.dto.request.PostCreateRequest;
import com.tave.alarmissue.post.dto.request.PostUpdateRequest;
import com.tave.alarmissue.post.dto.response.*;
import com.tave.alarmissue.post.repository.*;
import com.tave.alarmissue.post.converter.PostConverter;
import com.tave.alarmissue.post.domain.Post;
import com.tave.alarmissue.post.exception.PostException;
import com.tave.alarmissue.user.domain.UserEntity;
import com.tave.alarmissue.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

import static com.tave.alarmissue.post.exception.PostErrorCode.*;

@RequiredArgsConstructor
@Service
@Slf4j
@Transactional(readOnly = true)
@EnableAsync
public class PostService {

    private final PostConverter postConverter;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final VoteRepository voteRepository;
    private final CommentRepository commentRepository;
    private final NewsRepository newsRepository;

    // 게시글 작성
    @Transactional
    public PostResponse createPost(PostCreateRequest dto, Long userId) {
        UserEntity user = getUserById(userId);

        Post post = postConverter.toPost(dto, user);
        Post saved = postRepository.save(post);

        return PostConverter.toPostResponseDto(saved);

    }

    //게시글 수정
    @Transactional
    public PostResponse updatePost(Long postId, PostUpdateRequest dto, Long userId) {
        UserEntity user = getUserById(userId);
        Post post = getPostById(postId);

        if (!Objects.equals(post.getUser().getId(), user.getId())) {
            throw new PostException(POST_EDIT_FORBIDDEN, "post의 userId: " + post.getUser().getId() + " userId: " + user.getId());
        }

        News news = null;
        if (dto.getNewsId() != null) {
            news = newsRepository.findById(dto.getNewsId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid newsId: " + dto.getNewsId()));
        }

        post.Update(dto.getPostTitle(), dto.getPostContent(), dto.getThema(), dto.isHasVote(), news);

        //투표기능끄면 post와 연관된 vote DB삭제
        if (!post.getHasVote()) voteRepository.deleteAllByPost(post);
        Post saved = postRepository.save(post);
        return PostConverter.toPostResponseDto(saved);

    }

    //게시글 삭제
    @Transactional
    public void deletePost(Long postId, Long userId) {
        UserEntity user = getUserById(userId);
        Post post = getPostById(postId);

        if (!Objects.equals(post.getUser().getId(), user.getId())) {
            throw new PostException(POST_DELETE_FORBIDDEN, "post의 userId: " + post.getUser().getId() + " userId: " + user.getId());
        }

        postRepository.delete(post);

    }

    //게시글 상세조회
    public PostDetailResponse getPostDetail(Long postId, Long userId) {

        Post post = getPostById(postId);

        List<CommentResponse> commentResponses = getCommentResponses(post);

        VoteResponse voteResponse = createVoteResponse(post, userId);

        return PostConverter.toPostDetailResponseDto(post, voteResponse, commentResponses);
    }


    /*
    private method 분리
     */

    private UserEntity getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new PostException(USER_ID_NOT_FOUND, "userId" + userId));
    }

    private Post getPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new PostException(POST_ID_NOT_FOUND," postId: " + postId));
    }

    private List<CommentResponse> getCommentResponses(Post post) {
        List<PostComment> comments = commentRepository.findAllByPostWithUserAndReplies(post);
        return PostCommentConverter.toCommentResponseDtos(comments);
    }

    private VoteResponse createVoteResponse(Post post, Long userId) {
        if (!post.getHasVote()) {
            return null;
        }

        VoteType myVoteType = null;
        if (userId != null) {
            UserEntity user = getUserById(userId);
            myVoteType = voteRepository.findByUserAndPost(user, post)
                    .map(PostVote::getVoteType)
                    .orElse(null);
        }

        List<VoteCountResponse> voteCounts = voteRepository.countVotesByType(post);
        return PostVoteConverter.toVoteResponseDto(post, myVoteType, voteCounts);
    }


    @Async
    @Transactional
    public void incrementViewCountAsync(Long postId) {
        postRepository.incrementViewCount(postId);
    }
}