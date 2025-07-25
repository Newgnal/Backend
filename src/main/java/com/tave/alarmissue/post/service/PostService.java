package com.tave.alarmissue.post.service;

import com.tave.alarmissue.news.domain.News;
import com.tave.alarmissue.news.domain.enums.Thema;
import com.tave.alarmissue.news.repository.NewsRepository;
import com.tave.alarmissue.post.converter.PostCommentConverter;
import com.tave.alarmissue.post.converter.PostVoteConverter;
import com.tave.alarmissue.post.domain.*;
import com.tave.alarmissue.post.domain.enums.TargetType;
import com.tave.alarmissue.post.domain.enums.VoteType;
import com.tave.alarmissue.post.dto.request.PostCreateRequest;
import com.tave.alarmissue.post.dto.request.PostUpdateRequest;
import com.tave.alarmissue.post.dto.response.*;
import com.tave.alarmissue.post.repository.*;
import com.tave.alarmissue.post.converter.PostConverter;
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

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

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
    private final LikeRepository likeRepository;
    // 게시글 작성
    @Transactional
    public PostResponse createPost(PostCreateRequest dto, Long userId) {
        UserEntity user = getUserById(userId);
        News news= getNewsById(dto.getNewsId());

        Post post = postConverter.toPost(dto, user, news);
        Post saved = postRepository.save(post);

        return PostConverter.toPostResponseDto(saved,false);

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
        String newsUrl = null;

        if (dto.getNewsId() != null) {
            news = getNewsById(dto.getNewsId());
            newsUrl = news.getUrl();
        }


        post.Update(dto.getPostTitle(), dto.getPostContent(), dto.getThema(), dto.isHasVote(), news, newsUrl);

        //투표기능끄면 post와 연관된 vote DB 삭제
        if (!post.getHasVote()) voteRepository.deleteAllByPost(post);
        Post saved = postRepository.save(post);

        boolean isLiked = isPostLikedByUser(user, post);

        return PostConverter.toPostResponseDto(saved,isLiked);

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

        UserEntity user = getUserByIdOrNull(userId);

        VoteResponse voteResponse = createVoteResponse(post, userId);

        boolean isLiked = isPostLikedByUser(user, post);
        List<CommentResponse> commentResponses = getCommentResponses(post,user);

        return PostConverter.toPostDetailResponseDto(post, voteResponse, commentResponses, isLiked);
    }


    /*
    private method 분리
     */

    private UserEntity getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new PostException(USER_ID_NOT_FOUND, "userId" + userId));
    }
    //조회용
    private UserEntity getUserByIdOrNull(Long userId) {
        if (userId == null) {
            return null;
        }
        return userRepository.findById(userId).orElse(null);
    }

    private News getNewsById(Long newsId) {
        if (newsId == null) {
            return null;
        }
        return newsRepository.findById(newsId)
                .orElseThrow(() -> new PostException(NEWS_ID_NOT_FOUND, "newsId: " + newsId));
    }


    private Post getPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new PostException(POST_ID_NOT_FOUND," postId: " + postId));
    }

    private List<CommentResponse> getCommentResponses(Post post, UserEntity user) {
        // 댓글 조회
        List<PostComment> comments = commentRepository.findAllByPostWithUserAndReplies(post);

        // 댓글 ID 리스트 추출
        List<Long> commentIds = comments.stream()
                .map(PostComment::getCommentId)
                .toList();

        // 댓글 좋아요 맵 준비
        Map<Long, Boolean> commentLikeMap = Collections.emptyMap();

        // 대댓글 리스트를 댓글 ID 기준으로 모두 모으기
        List<PostReply> replies = comments.stream()
                .flatMap(c -> c.getReplies().stream())
                .toList();

        // 대댓글 ID 리스트 추출
        List<Long> replyIds = replies.stream()
                .map(PostReply::getReplyId)
                .toList();

        // 대댓글 좋아요 맵 준비
        Map<Long, Boolean> replyLikeMap = Collections.emptyMap();

        if (user != null) {
            // 댓글 좋아요 상태 조회
            commentLikeMap = likeRepository.findAllByUserAndTargetTypeAndCommentIds(
                            user, TargetType.COMMENT , commentIds).stream()
                    .collect(Collectors.toMap(pl -> pl.getComment().getCommentId(), pl -> true));

            // 대댓글 좋아요 상태 조회
            replyLikeMap = likeRepository.findAllByUserAndTargetTypeAndReplyIds(
                            user, TargetType.REPLY, replyIds).stream()
                    .collect(Collectors.toMap(pl -> pl.getPostReply().getReplyId(), pl -> true));
        }


        // 댓글 + 대댓글 좋아요 여부 맵을 넘겨서 변환
        return PostCommentConverter.toCommentResponseDtos(comments, commentLikeMap, replyLikeMap);
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

    private boolean isPostLikedByUser(UserEntity user, Post post) {
        return likeRepository.findPostLike(user, post)
                .isPresent();
    }

    @Async
    @Transactional
    public void incrementViewCountAsync(Long postId) {
        postRepository.incrementViewCount(postId);
    }
}