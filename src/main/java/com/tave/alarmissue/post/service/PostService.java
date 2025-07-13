package com.tave.alarmissue.post.service;

import com.tave.alarmissue.news.domain.enums.Thema;
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
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final ReplyRepository replyRepository;

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

        post.Update(dto.getPostTitle(), dto.getPostContent(), dto.getArticleUrl(), dto.getThema(), dto.isHasVote());

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

    @Transactional
    public PostDetailResponse getPostDetail(Long postId, Long userId) {

        UserEntity user = getUserById(userId);
        Post post = getPostById(postId);

        List<PostReply> replies = replyRepository.findAllByPost(post);
        List<PostComment> comments = commentRepository.findAllByPost(post);
        //사용자

        List<CommentResponse> commentResponses = comments.stream()
                .map(comment -> {
                    List<PostReply> repliesForComment = replies.stream()
                            .filter(reply -> reply.getPostComment().getCommentId().equals(comment.getCommentId()))
                            .toList();

                    return PostCommentConverter.toCommentResponseDto(comment, repliesForComment);
                })
                .toList();

        postRepository.incrementViewCount(postId); //조회수 증가

        if(!post.getHasVote()){
            return PostConverter.toPostDetailResponseDto(post,null,commentResponses);
        }
        else {
            VoteType myVoteType = voteRepository.findByUserAndPost(user, post)
                    .map(PostVote::getVoteType)
                    .orElse(null);

            List<VoteCountResponse> voteCounts = voteRepository.countVotesByType(post);
            VoteResponse voteResponse = PostVoteConverter.toVoteResponseDto(post, myVoteType, voteCounts);

            return PostConverter.toPostDetailResponseDto(post, voteResponse, commentResponses);
        }
    }

    //전체 게시글 조회
    public Page<PostResponse> getAllPost(Pageable pageable) {

        Page<Post> posts = postRepository.findAll(pageable);
        return PostConverter.toPostResponseDtos(posts);
    }

    //게시글 조회 조회순
    public Page<PostResponse> getHotPost(Pageable pageable) {
        Page<Post> posts = postRepository.findAll(pageable);
        return PostConverter.toPostResponseDtos(posts);
    }

    //테마별 조회
    public Page<PostResponse> getPostByThema(Thema thema, Pageable pageable) {
        Page<Post> posts = postRepository.findAllByThema(thema, pageable);
        return PostConverter.toPostResponseDtos(posts);
    }

    //테마별 조회 인기순
    public Page<PostResponse> getHotPostByThema(Thema thema, Pageable pageable) {
        Page<Post> posts = postRepository.findAllByThema(thema, pageable);
        return PostConverter.toPostResponseDtos(posts);
    }

    private UserEntity getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new PostException(USER_ID_NOT_FOUND, "userId" + userId));
    }
    private Post getPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new PostException(POST_ID_NOT_FOUND," postId: " + postId));
    }
}