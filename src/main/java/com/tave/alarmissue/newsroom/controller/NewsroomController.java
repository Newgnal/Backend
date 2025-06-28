package com.tave.alarmissue.newsroom.controller;

import com.tave.alarmissue.auth.dto.request.PrincipalUserDetails;
import com.tave.alarmissue.global.dto.request.PagenationRequest;
import com.tave.alarmissue.newsroom.dto.request.KeywordRequest;
import com.tave.alarmissue.newsroom.dto.response.KeywordResponse;
import com.tave.alarmissue.newsroom.dto.response.KeywordNewsResponse;
import com.tave.alarmissue.newsroom.service.NewsroomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.nio.file.attribute.UserPrincipal;
import java.util.Map;

@RestController
@RequestMapping("/newsroom/v1")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "뉴스룸 API")
public class NewsroomController {

    private final NewsroomService newsroomService;

    // 키워드 등록
    @PostMapping("/keywords")
    @Operation(summary = "키워드 등록", description = "사용자가 관심 키워드를 등록합니다. 키워드는 2자 이상 10자 이하로 입력해야 합니다.")
    public ResponseEntity<KeywordResponse> addKeyword( @AuthenticationPrincipal PrincipalUserDetails principal,
                                                      @Valid @RequestBody KeywordRequest request) {
        Long userId = principal.getUserId();
        KeywordResponse response = newsroomService.addKeyword(userId, request.getKeyword());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    // 사용자별 키워드 삭제
    @DeleteMapping("keywords/{keywordId}")
    @Operation(summary = "키워드 삭제", description = "사용자가 등록한 특정 키워드를 삭제합니다.")
    public ResponseEntity<Void> removeKeyword(@AuthenticationPrincipal PrincipalUserDetails principal,
                                                @PathVariable Long keywordId) {
        Long userId = principal.getUserId();
        newsroomService.removeKeyword(userId, keywordId);
        return ResponseEntity.ok().build();
    }

    // 사용자의 키워드별 뉴스 개수 조회
    @Operation(summary = "키워드별 뉴스 개수 조회", description = "사용자가 등록한 모든 키워드별로 관련 뉴스 개수를 조회합니다.")
    @GetMapping("/keywords/count")
    public ResponseEntity<Map<String, Long>> getUserKeywordNewsCount(@AuthenticationPrincipal PrincipalUserDetails principal) {
        Long userId = principal.getUserId();
        Map<String, Long> response = newsroomService.getUserKeywordNewsCount(userId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 특정 키워드의 뉴스 조회
    @GetMapping("/keywords/{keywordId}/news")
    @Operation(summary = "키워드별 뉴스 조회", description = "사용자가 등록한 특정 키워드와 관련된 뉴스 목록을 조회합니다. 최신 뉴스부터 정렬되어 반환됩니다.")
    public ResponseEntity<KeywordNewsResponse> getNewsByKeyword(
            @Parameter(description = "사용자 ID", required = true, example = "1")
            @AuthenticationPrincipal PrincipalUserDetails principal,
            @Parameter(description = "조회할 키워드 ID", required = true, example = "1")
            @PathVariable Long keywordId,
            @Parameter(description = "마지막 뉴스 ID (무한 스크롤용)", example = "100")
            @RequestParam(required = false) Long lastId,
            @Parameter(description = "페이지 크기", example = "10")
            @RequestParam(defaultValue = "10") @Min(1) @Max(50) Integer size) {

        Long userId = principal.getUserId();

        PagenationRequest paginationRequest = PagenationRequest.builder()
                .lastId(lastId)
                .size(size)
                .build();

        KeywordNewsResponse response = newsroomService.getNewsByKeyword(userId, keywordId, paginationRequest);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
