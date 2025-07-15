package com.tave.alarmissue.post.converter;

import com.tave.alarmissue.post.domain.Post;
import com.tave.alarmissue.post.domain.PostComment;
import com.tave.alarmissue.post.domain.PostReply;
import com.tave.alarmissue.post.dto.request.PostCreateRequest;
import com.tave.alarmissue.post.dto.response.*;
import com.tave.alarmissue.user.domain.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@RequiredArgsConstructor
public class
PostConverter {

    public static PostResponse toPostResponseDto(Post post) {
        return PostResponse.builder()
                .postId(post.getPostId())
                .postTitle(post.getPostTitle())
                .postContent(post.getPostContent())
                .likeCount(post.getLikeCount())
                .articleUrl(post.getArticleUrl())
                .thema(post.getThema())
                .nickname(post.getUser().getNickName())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .hasVote(post.getHasVote())
                .viewCount(post.getViewCount())
                .commentCount(post.getCommentCount())
                .build();
    }

    //게시글 상세 조회
    public static PostDetailResponse toPostDetailResponseDto(Post post,
                                                             VoteResponse vote,
                                                             List<CommentResponse> comments) {
        PostResponse postResponseDto = toPostResponseDto(post);

        return PostDetailResponse.builder()
                .post(postResponseDto)
                .vote(vote)
                .comments(comments)
                .build();
    }
    //홈화면 조회
    public static PostHomeResponse toPostHomeResponseDto(List<ThemeCountResponse> topThemes,
                                                         List<PostResponse> hotPostResponse,
                                                         List<PostResponse> postResponse) {
        return PostHomeResponse.builder().
                topThemes(topThemes).
                hotPostResponse(hotPostResponse).
                postResponse(postResponse).
                build();
    }


    public static Page<PostResponse> toPostResponseDtos(Page<Post> posts) {
        return posts.map(post -> PostResponse.builder()
                .postId(post.getPostId())
                .postTitle(post.getPostTitle())
                .postContent(post.getPostContent())
                .likeCount(post.getLikeCount())
                .articleUrl(post.getArticleUrl())
                .thema(post.getThema())
                .nickname(post.getUser().getNickName())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .hasVote(post.getHasVote())
                .viewCount(post.getViewCount())
                .commentCount(post.getCommentCount())
                .build()
        );
    }
    //인기 게시글
    public static PostResponse toPostHotResponseDto(Post post){
        return PostResponse.builder()
               .postId(post.getPostId())
                .postTitle(post.getPostTitle())
                .likeCount(post.getLikeCount())
                .thema(post.getThema())
                .viewCount(post.getViewCount())
                .commentCount(post.getCommentCount())
                .build();
    }

    public Post toPost(PostCreateRequest dto, UserEntity user) {
        return Post.builder()
                .postTitle(dto.getPostTitle())
                .postContent(dto.getPostContent())
                .articleUrl(dto.getArticleUrl())
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
