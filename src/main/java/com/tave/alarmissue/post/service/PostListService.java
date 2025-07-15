package com.tave.alarmissue.post.service;

import com.tave.alarmissue.news.domain.enums.Thema;
import com.tave.alarmissue.post.converter.PostConverter;
import com.tave.alarmissue.post.domain.Post;
import com.tave.alarmissue.post.dto.response.HotPostResponse;
import com.tave.alarmissue.post.dto.response.PostHomeResponse;
import com.tave.alarmissue.post.dto.response.PostResponse;
import com.tave.alarmissue.post.dto.response.ThemeCountResponse;
import com.tave.alarmissue.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
@Transactional(readOnly = true)
public class PostListService {

    private final PostRepository postRepository;

    //전체 게시글 조회
    public Page<PostResponse> getAllPost(Pageable pageable) {

        Page<Post> posts = postRepository.findAll(pageable);
        return PostConverter.toPostResponseDtos(posts);
    }

    //테마별 조회
    public Page<PostResponse> getPostByThema(Thema thema, Pageable pageable) {
        Page<Post> posts = postRepository.findAllByThema(thema, pageable);
        return PostConverter.toPostResponseDtos(posts);
    }

    //홈화면 조회
    public PostHomeResponse getPostHome() {

        //인기테마 3개 가져오기
        List<ThemeCountResponse> topThemes = postRepository.findTop3Themes();

        //인기 게시글 9개 가져오기
        List<HotPostResponse> hotPostResponse  = postRepository.findTop9ByOrderByViewCountDesc()
                .stream()
                .map(PostConverter::toPostHotResponseDto)
                .toList();

        //최근 게시글 4개 가져오기
        List<PostResponse> postResponse = postRepository.findTop4ByOrderByCreatedAtDesc()
                .stream()
                .map(PostConverter::toPostResponseDto)
                .toList();

        return PostConverter.toPostHomeResponseDto(topThemes,hotPostResponse,postResponse);
    }


}
