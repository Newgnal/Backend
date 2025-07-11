package com.tave.alarmissue.post.service;

import com.tave.alarmissue.post.converter.CommentConverter;
import com.tave.alarmissue.post.domain.Comment;
import com.tave.alarmissue.post.dto.request.CommentCreateRequestDto;
import com.tave.alarmissue.post.dto.response.CommentResponseDto;
import com.tave.alarmissue.post.exception.CommentException;
import com.tave.alarmissue.post.repository.*;
import com.tave.alarmissue.post.domain.Post;
import com.tave.alarmissue.user.domain.UserEntity;
import com.tave.alarmissue.user.repository.UserRepository;
import com.tave.alarmissue.post.domain.Vote;
import com.tave.alarmissue.post.domain.VoteType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

import static com.tave.alarmissue.post.exception.CommentErrorCode.*;

@RequiredArgsConstructor
@Service
@Slf4j
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentConverter commentConverter;
    private final VoteRepository voteRepository;
    private final LikeRepository likeRepository;
   private final ReplyRepository replyRepository;

    @Transactional
    public CommentResponseDto createComment(CommentCreateRequestDto dto, Long userId, Long postId) {
        UserEntity user = userRepository.findById(userId).
                orElseThrow(()-> new CommentException(USER_ID_NOT_FOUND,"사용자가 없습니다"));

        Post post= postRepository.findById(postId).
                orElseThrow(()->new CommentException(POST_ID_NOT_FOUND, "postId:" + postId));

        
        Vote vote = null;
        VoteType voteType = null;
        //게시글에 투표가 있고 실제 유저가 투표했을경우 voteType 넣기
        if (post.getHasVote()) { 
            vote = voteRepository.findByUserAndPost(user, post).orElse(null);
            voteType = (vote != null) ? vote.getVoteType() : null;
        }

        Comment comment = commentConverter.toComment(dto, user, post, voteType);


        Comment saved = commentRepository.save(comment);
        return CommentConverter.toCommentResponseDto(saved);
    }
    @Transactional
    public void deleteComment(Long commentId, Long userId, Long postId) {
        UserEntity user = userRepository.findById(userId).
                orElseThrow(()->new CommentException(USER_ID_NOT_FOUND,"사용자가 없습니다."));

        Post post= postRepository.findById(postId).
                orElseThrow(()->new CommentException(POST_ID_NOT_FOUND, "postId:" + postId));

        Comment comment = commentRepository.findById(commentId).
                orElseThrow(()->new CommentException(COMMENT_ID_NOT_FOUND, "commentId: "+ commentId));

        if(!Objects.equals(post.getPostId(),comment.getPost().getPostId())) {
            throw new CommentException(COMMENT_DELETE_FORBIDDEN, "comment의 postId: "+comment.getPost().getPostId() + " postId: " + post.getPostId());
        }

        if(!Objects.equals(comment.getUser().getId(), user.getId())) {
            throw new CommentException(COMMENT_DELETE_FORBIDDEN, "comment의 userId: "+comment.getUser().getId() + " userId: " + user.getId());
        }
        likeRepository.deleteAllByComment(comment); //좋아요 삭제
        replyRepository.deleteAllByComment(comment); //대댓글 삭제
        commentRepository.delete(comment); //댓글 삭제
    }
}
