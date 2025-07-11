package com.tave.alarmissue.post.converter;

import com.tave.alarmissue.post.domain.Comment;
import com.tave.alarmissue.post.domain.Post;
import com.tave.alarmissue.post.dto.request.PostCreateRequestDto;
import com.tave.alarmissue.post.dto.response.PostDetailResponseDto;
import com.tave.alarmissue.post.dto.response.PostResponseDto;
import com.tave.alarmissue.user.domain.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import com.tave.alarmissue.post.dto.response.CommentResponseDto;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class
PostConverter {

    private final CommentConverter commentConverter;

    public static PostResponseDto toPostResponseDto(Post post) {
        return PostResponseDto.builder()
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
                .build();
    }

    public static PostDetailResponseDto toPostDetailResponseDto(Post post, List<Comment> comments) {
        PostResponseDto postResponseDto = toPostResponseDto(post);

        List<CommentResponseDto> commentResponseDto = CommentConverter.toCommentResponseDtos(comments);

        return PostDetailResponseDto.builder()
                .post(postResponseDto)
                .comments(commentResponseDto)
                .build();
    }

    public static Page<PostResponseDto> toPostResponseDtos(Page<Post> posts) {
        return posts.map(post -> PostResponseDto.builder()
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
                .build()
        );
    }

    public Post toPost(PostCreateRequestDto dto, UserEntity user) {
        return Post.builder()
                .postTitle(dto.getPostTitle())
                .postContent(dto.getPostContent())
                .articleUrl(dto.getArticleUrl())
                .thema(dto.getThema())
                .hasVote(dto.isHasVote())
                .likeCount(0L)
                .viewCount(0L)
                .user(user)
                .build();
    }

}
