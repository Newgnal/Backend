package com.tave.alarmissue.post.dto.response;

import com.tave.alarmissue.news.domain.enums.Thema;
import com.tave.alarmissue.post.domain.Post;
import com.tave.alarmissue.post.repository.ThemeCountProjection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class PostHomeResponse {
    private List<ThemeCountResponse> topThemes;
    private List<PostResponse> hotPostResponse;
    private List<PostResponse> postResponse;

}
