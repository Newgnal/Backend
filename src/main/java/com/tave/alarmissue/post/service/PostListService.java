package com.tave.alarmissue.post.service;

import com.tave.alarmissue.news.domain.enums.Thema;
import com.tave.alarmissue.post.converter.PostConverter;
import com.tave.alarmissue.post.domain.Post;
import com.tave.alarmissue.post.domain.PostLike;
import com.tave.alarmissue.post.domain.enums.TargetType;
import com.tave.alarmissue.post.dto.response.HotPostResponse;
import com.tave.alarmissue.post.dto.response.PostHomeResponse;
import com.tave.alarmissue.post.dto.response.PostResponse;
import com.tave.alarmissue.post.dto.response.ThemeCountResponse;
import com.tave.alarmissue.post.repository.LikeRepository;
import com.tave.alarmissue.post.repository.PostRepository;
import com.tave.alarmissue.user.domain.UserEntity;
import com.tave.alarmissue.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
@Transactional(readOnly = true)
public class PostListService {

    private final PostRepository postRepository;
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;

    //전체 게시글 조회
    public Page<PostResponse> getAllPost(Pageable pageable,Long userId) {

        Page<Post> posts = postRepository.findAll(pageable);
        Map<Long, Boolean> postIdToIsLikedMap = Collections.emptyMap();
        UserEntity user=getUserByIdOrNull(userId);
        if (user != null) {
            List<Long> postIds = posts.stream()
                    .map(Post::getPostId)
                    .toList();

            postIdToIsLikedMap = likeRepository.findAllByUserAndTargetTypeAndPostIds(user, TargetType.POST, postIds).stream()
                    .collect(Collectors.toMap(pl -> pl.getPost().getPostId(), pl -> true));
        }
        return PostConverter.toPostResponseDtos(posts,postIdToIsLikedMap);
    }

    //테마별 조회
    public Page<PostResponse> getPostByThema(Thema thema, Pageable pageable,Long userId) {
        Page<Post> posts = postRepository.findAllByThema(thema, pageable);
        Map<Long, Boolean> postIdToIsLikedMap = Collections.emptyMap();
        UserEntity user = getUserByIdOrNull(userId);
        if (user != null) {
            List<Long> postIds = posts.stream()
                    .map(Post::getPostId)
                    .toList();

            postIdToIsLikedMap = likeRepository.findAllByUserAndTargetTypeAndPostIds(
                            user, TargetType.POST, postIds).stream()
                    .collect(Collectors.toMap(pl -> pl.getPost().getPostId(), pl -> true));
        }
        return PostConverter.toPostResponseDtos(posts,postIdToIsLikedMap);
    }

    //홈화면 조회
    public PostHomeResponse getPostHome(Long userId) {

        //인기테마 3개 가져오기
        List<ThemeCountResponse> topThemes = postRepository.findTop3Themes();

        UserEntity user = getUserByIdOrNull(userId);

        //인기 게시글 9개 가져오기
        List<Post> hotPosts = postRepository.findTop9ByOrderByViewCountDesc();
        Map<Long, Boolean> hotPostLikeMap;
        if (user != null) {
            List<Long> hotPostIds = hotPosts.stream()
                    .map(Post::getPostId)
                    .toList();

            hotPostLikeMap = likeRepository.findAllByUserAndTargetTypeAndPostIds(
                            user, TargetType.POST, hotPostIds).stream()
                    .collect(Collectors.toMap(pl -> pl.getPost().getPostId(), pl -> true));
        } else {
            hotPostLikeMap = Collections.emptyMap();
        }
        List<HotPostResponse> hotPostResponse = hotPosts.stream()
                .map(post -> {
                    boolean isLiked = hotPostLikeMap.getOrDefault(post.getPostId(), false);
                    return PostConverter.toPostHotResponseDto(post,isLiked);
                })
                .toList();


        //최근 게시글 4개 가져오기
        List<Post> recentPosts = postRepository.findTop4ByOrderByCreatedAtDesc();

        Map<Long, Boolean> recentPostLikeMap;

        if (user != null) {
            List<Long> recentPostIds = recentPosts.stream()
                    .map(Post::getPostId)
                    .toList();

            recentPostLikeMap = likeRepository.findAllByUserAndTargetTypeAndPostIds(
                            user, TargetType.POST, recentPostIds).stream()
                    .collect(Collectors.toMap(pl -> pl.getPost().getPostId(), pl -> true));
        } else {
            recentPostLikeMap = Collections.emptyMap();
        }
        List<PostResponse> postResponse = recentPosts.stream()
                .map(post -> {
                    boolean isLiked = recentPostLikeMap.getOrDefault(post.getPostId(), false);
                    return PostConverter.toPostResponseDto(post, isLiked);
                })
                .toList();
        return PostConverter.toPostHomeResponseDto(topThemes,hotPostResponse,postResponse);
    }

    private UserEntity getUserByIdOrNull(Long userId) {
        if (userId == null) {
            return null;
        }
        return userRepository.findById(userId).orElse(null);
    }


}
