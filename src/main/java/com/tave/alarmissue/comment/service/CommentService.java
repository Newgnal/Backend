package com.tave.alarmissue.comment.service;

import com.tave.alarmissue.comment.converter.CommentConverter;
import com.tave.alarmissue.comment.domain.Comment;
import com.tave.alarmissue.comment.dto.request.CommentCreateRequestDto;
import com.tave.alarmissue.comment.dto.response.CommentResponseDto;
import com.tave.alarmissue.comment.exception.CommentException;
import com.tave.alarmissue.comment.repository.CommentRepository;
import com.tave.alarmissue.post.domain.Post;
import com.tave.alarmissue.post.repository.PostRepository;
import com.tave.alarmissue.user.domain.UserEntity;
import com.tave.alarmissue.user.repository.UserRepository;
import com.tave.alarmissue.vote.domain.Vote;
import com.tave.alarmissue.vote.domain.VoteType;
import com.tave.alarmissue.vote.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.tave.alarmissue.comment.exception.CommentErrorCode.*;

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

        commentRepository.delete(comment);
    }
}
