package com.tave.alarmissue.post.controller;

import com.tave.alarmissue.auth.dto.request.PrincipalUserDetails;
import com.tave.alarmissue.news.domain.enums.Thema;
import com.tave.alarmissue.post.converter.PostConverter;
import com.tave.alarmissue.post.domain.enums.SortType;
import com.tave.alarmissue.post.dto.response.PageResponse;
import com.tave.alarmissue.post.dto.response.PostDetailResponse;
import com.tave.alarmissue.post.dto.response.PostHomeResponse;
import com.tave.alarmissue.post.dto.response.PostResponse;
import com.tave.alarmissue.post.service.PostListService;
import com.tave.alarmissue.post.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.function.Function;

@RequiredArgsConstructor
@RestController
@RequestMapping("/post/v1/list")
public class PostLIstController {

    private final PostService postService;
    private final PostConverter postConverter;
    private final PostListService postListService;

    //게시글 전체 조회
    @GetMapping
    @Operation(summary = "게시글 전체조회", description = "게시글을 전체 조회합니다(최신순)")
    public ResponseEntity<PageResponse<PostResponse>> getAllPost(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "LATEST") SortType sortType
    ) {
        return getSortedPosts(page, size, sortType, pageable -> postListService.getAllPost(pageable));
    }


    //게시글 테마별 조회
    @GetMapping("/thema/{thema}")
    @Operation(summary = "게시글 테마 별 조회", description = "해당 테마의 게시글들을 조회합니다(최신순)")
    public ResponseEntity<PageResponse<PostResponse>> getPostByThema(
            @PathVariable Thema thema,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "LATEST") SortType sortType
    ) {
        return getSortedPosts(page, size, sortType, pageable -> postListService.getPostByThema(thema, pageable));
    }


    @GetMapping("/home")
    @Operation(summary = "게시글 홈 화면 조회", description = "인기 테마3개(게시글 순), 인기글 9개(조회수순), 최신 글 4개를 조회합니다")
    public ResponseEntity<PostHomeResponse> getPostHome(){
        PostHomeResponse responseDto = postListService.getPostHome();
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    /*
    private method 분리 (페이징)
     */

    private ResponseEntity<PageResponse<PostResponse>> getSortedPosts(
            int page,
            int size,
            SortType sortType,
            Function<Pageable, Page<PostResponse>> pageFetcher
    ) {
        Pageable sortedPageable = PageRequest.of(page, size, sortType.getSort());
        Page<PostResponse> postPage = pageFetcher.apply(sortedPageable);
        PageResponse<PostResponse> responseDto = postConverter.toPageResponse(postPage);
        return ResponseEntity.ok(responseDto);
    }

}
