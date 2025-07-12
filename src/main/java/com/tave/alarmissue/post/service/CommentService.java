package com.tave.alarmissue.post.service;

import com.tave.alarmissue.post.converter.PostCommentConverter;
import com.tave.alarmissue.post.domain.PostComment;
import com.tave.alarmissue.post.dto.request.CommentCreateRequest;
import com.tave.alarmissue.post.dto.response.CommentResponse;
import com.tave.alarmissue.post.exception.PostException;
import com.tave.alarmissue.post.repository.*;
import com.tave.alarmissue.post.domain.Post;
import com.tave.alarmissue.user.domain.UserEntity;
import com.tave.alarmissue.user.repository.UserRepository;
import com.tave.alarmissue.post.domain.PostVote;
import com.tave.alarmissue.post.domain.enums.VoteType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

import static com.tave.alarmissue.post.exception.PostErrorCode.*;

@RequiredArgsConstructor
@Service
@Slf4j
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final PostCommentConverter commentConverter;
    private final VoteRepository voteRepository;
    private final LikeRepository likeRepository;
   private final ReplyRepository replyRepository;

    @Transactional
    public CommentResponse createComment(CommentCreateRequest dto, Long userId, Long postId) {
        UserEntity user = userRepository.findById(userId).
                orElseThrow(()-> new PostException(USER_ID_NOT_FOUND,"사용자가 없습니다"));

        Post post= postRepository.findById(postId).
                orElseThrow(()->new PostException(POST_ID_NOT_FOUND, "postId:" + postId));

        
        PostVote vote = null;
        VoteType voteType = null;
        //게시글에 투표가 있고 실제 유저가 투표했을경우 voteType 넣기
        if (post.getHasVote()) { 
            vote = voteRepository.findByUserAndPost(user, post).orElse(null);
            voteType = (vote != null) ? vote.getVoteType() : null;
        }

        PostComment postComment = commentConverter.toComment(dto, user, post, voteType);


        PostComment saved = commentRepository.save(postComment);
        return PostCommentConverter.toCommentResponseDto(saved);
    }
    @Transactional
    public void deleteComment(Long commentId, Long userId, Long postId) {
        UserEntity user = userRepository.findById(userId).
                orElseThrow(()->new PostException(USER_ID_NOT_FOUND,"사용자가 없습니다."));

        Post post= postRepository.findById(postId).
                orElseThrow(()->new PostException(POST_ID_NOT_FOUND, "postId:" + postId));

        PostComment postComment = commentRepository.findById(commentId).
                orElseThrow(()->new PostException(COMMENT_ID_NOT_FOUND, "commentId: "+ commentId));

        if(!Objects.equals(post.getPostId(), postComment.getPost().getPostId())) {
            throw new PostException(COMMENT_DELETE_FORBIDDEN, "comment의 postId: "+ postComment.getPost().getPostId() + " postId: " + post.getPostId());
        }

        if(!Objects.equals(postComment.getUser().getId(), user.getId())) {
            throw new PostException(COMMENT_DELETE_FORBIDDEN, "comment의 userId: "+ postComment.getUser().getId() + " userId: " + user.getId());
        }
        likeRepository.deleteAllByComment(postComment); //좋아요 삭제
        replyRepository.deleteAllByPostComment(postComment); //대댓글 삭제
        commentRepository.delete(postComment); //댓글 삭제
    }
}
