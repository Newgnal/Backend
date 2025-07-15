package com.tave.alarmissue.post.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class PostHomeResponse {
    private List<ThemeCountResponse> topThemes;
    private List<HotPostResponse> hotPostResponse;
    private List<PostResponse> postResponse;

}
