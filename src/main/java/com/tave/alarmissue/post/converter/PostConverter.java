package com.tave.alarmissue.post.converter;

import com.tave.alarmissue.news.domain.News;
import com.tave.alarmissue.news.repository.NewsRepository;
import com.tave.alarmissue.post.domain.Post;
import com.tave.alarmissue.post.dto.request.PostCreateRequest;
import com.tave.alarmissue.post.dto.response.*;
import com.tave.alarmissue.user.domain.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class
PostConverter {

    private final NewsRepository newsRepository;

    public static PostResponse toPostResponseDto(Post post,boolean isLiked) {
        return PostResponse.builder()
                .postId(post.getPostId())
                .postTitle(post.getPostTitle())
                .postContent(post.getPostContent())
                .likeCount(post.getLikeCount())
                .newsId(post.getNews() != null ? post.getNews().getId() : null)
                .newsUrl(post.getNewsUrl())
                .thema(post.getThema())
                .nickname(post.getUser().getNickName())
                .imageUrl(post.getUser().getImageUrl())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .hasVote(post.getHasVote())
                .viewCount(post.getViewCount())
                .commentCount(post.getCommentCount())
                .isLiked(isLiked)
                .build();
    }

    //게시글 상세 조회
    public static PostDetailResponse toPostDetailResponseDto(Post post,
                                                             VoteResponse vote,
                                                             List<CommentResponse> comments,
                                                             boolean isLiked) {
        PostResponse postResponseDto = toPostResponseDto(post,isLiked);

        return PostDetailResponse.builder()
                .post(postResponseDto)
                .vote(vote)
                .comments(comments)
                .build();
    }

    //홈화면 조회
    public static PostHomeResponse toPostHomeResponseDto(List<ThemeCountResponse> topThemes,
                                                         List<HotPostResponse> hotPostResponse,
                                                         List<PostResponse> postResponse) {
        return PostHomeResponse.builder().
                topThemes(topThemes).
                hotPostResponse(hotPostResponse).
                postResponse(postResponse).
                build();
    }


    public static Page<PostResponse> toPostResponseDtos(Page<Post> posts, Map<Long, Boolean> postIdToIsLikedMap) {
        Map<Long, Boolean> finalMap = postIdToIsLikedMap == null ? Collections.emptyMap() : postIdToIsLikedMap;

        return posts.map(post -> PostResponse.builder()
                .postId(post.getPostId())
                .postTitle(post.getPostTitle())
                .postContent(post.getPostContent())
                .likeCount(post.getLikeCount())
                .newsId(post.getNews() != null ? post.getNews().getId() : null)
                .thema(post.getThema())
                .nickname(post.getUser().getNickName())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .hasVote(post.getHasVote())
                .viewCount(post.getViewCount())
                .commentCount(post.getCommentCount())
                .isLiked(finalMap.getOrDefault(post.getPostId(), false))
                .build()
        );
    }

    //인기 게시글
    public static HotPostResponse toPostHotResponseDto(Post post,boolean isLiked){
        return HotPostResponse.builder()
               .postId(post.getPostId())
                .postTitle(post.getPostTitle())
                .likeCount(post.getLikeCount())
                .thema(post.getThema())
                .viewCount(post.getViewCount())
                .commentCount(post.getCommentCount())
                .isLiked(isLiked)
                .build();
    }

    public Post toPost(PostCreateRequest dto, UserEntity user, News news) {

        return Post.builder()
                .postTitle(dto.getPostTitle())
                .postContent(dto.getPostContent())
                .news(news)
                .newsUrl(news != null ? news.getUrl() : null)
                .thema(dto.getThema())
                .hasVote(dto.isHasVote())
                .likeCount(0L)
                .viewCount(0L)
                .commentCount(0L)
                .user(user)
                .build();
    }

    public static PageResponse<PostResponse> toPageResponse(Page<PostResponse> postPage) {
        return PageResponse.<PostResponse>builder()
                .content(postPage.getContent())
                .pageNumber(postPage.getNumber())
                .pageSize(postPage.getSize())
                .last(postPage.isLast())
                .build();
    }
}
