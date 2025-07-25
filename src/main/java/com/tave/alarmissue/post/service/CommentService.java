package com.tave.alarmissue.post.service;

import com.tave.alarmissue.post.converter.PostCommentConverter;
import com.tave.alarmissue.post.converter.PostReplyConverter;
import com.tave.alarmissue.post.domain.PostComment;
import com.tave.alarmissue.post.domain.PostReply;
import com.tave.alarmissue.post.dto.request.CommentCreateRequest;
import com.tave.alarmissue.post.dto.request.ReplyCreateRequest;
import com.tave.alarmissue.post.dto.response.CommentResponse;
import com.tave.alarmissue.post.dto.response.ReplyResponse;
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

import java.util.Collections;
import java.util.Objects;

import static com.tave.alarmissue.post.exception.PostErrorCode.*;

@RequiredArgsConstructor
@Service
@Slf4j
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final ReplyRepository replyRepository;
    private final PostReplyConverter replyConverter;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final PostCommentConverter commentConverter;
    private final VoteRepository voteRepository;

    @Transactional
    public CommentResponse createComment(CommentCreateRequest dto, Long userId, Long postId) {

        UserEntity user = userRepository.findById(userId).
                orElseThrow(()-> new PostException(USER_ID_NOT_FOUND,"userId:"+userId));

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
        postRepository.incrementCommentCount(postId); //댓글 갯수 증가
        return PostCommentConverter.toCommentResponseDto(saved, Collections.emptyMap(),null,false);
    }


    @Transactional
    public void deleteComment(Long commentId, Long userId) {

        UserEntity user = userRepository.findById(userId).
                orElseThrow(()->new PostException(USER_ID_NOT_FOUND,"userId:"+userId));

        PostComment postComment = commentRepository.findById(commentId).
                orElseThrow(()->new PostException(COMMENT_ID_NOT_FOUND, "commentId: "+ commentId));

        if(!Objects.equals(postComment.getUser().getId(), user.getId())) {
            throw new PostException(COMMENT_DELETE_FORBIDDEN, "comment의 userId: "+ postComment.getUser().getId() + " userId: " + user.getId());
        }
        postRepository.decrementCommentCount(postComment.getPost().getPostId()); //댓글 갯수 감소
        commentRepository.delete(postComment); //댓글 삭제
    }

    @Transactional
    public ReplyResponse createReply(ReplyCreateRequest dto, Long commentId, Long userId) {
        UserEntity user = userRepository.findById(userId).
                orElseThrow(()->new PostException(USER_ID_NOT_FOUND,"사용자가 없습니다."));

        PostComment postComment = commentRepository.findById(commentId).
                orElseThrow(()->new PostException(COMMENT_ID_NOT_FOUND, "commentId: "+ commentId));

        Post post= postRepository.findById(postComment.getPost().getPostId()).
                orElseThrow(()->new PostException(POST_ID_NOT_FOUND, "postId:" + postComment.getPost().getPostId()));

        PostVote vote = null;
        VoteType voteType = null;
        //게시글에 투표가 있고 실제 유저가 투표했을경우 voteType 넣기
        if (post.getHasVote()) {
            vote = voteRepository.findByUserAndPost(user, post).orElse(null);
            voteType = (vote != null) ? vote.getVoteType() : null;
        }

        PostReply reply = replyConverter.toReply(dto,user,post, postComment,voteType);
        PostReply saved = replyRepository.save(reply);
        postRepository.incrementCommentCount(postComment.getPost().getPostId()); //댓글  갯수 증가
        return PostReplyConverter.toReplyResponseDto(saved,false);

    }

    @Transactional
    public void deleteReply(Long replyId, Long userId) {

        UserEntity user = userRepository.findById(userId).
                orElseThrow(()->new PostException(USER_ID_NOT_FOUND,"사용자가 없습니다."));

        PostReply reply = replyRepository.findById(replyId).
                orElseThrow(()-> new PostException(REPLY_ID_NOT_FOUND, "replyId:" + replyId));

        if(!Objects.equals(reply.getUser().getId(),user.getId())) {
            throw new PostException(REPLY_DELETE_FORBIDDEN,"Reply의 userId: "+reply.getUser().getId()+ " userId: " + user.getId());
        }
        postRepository.decrementCommentCount(reply.getPost().getPostId()); //댓글 갯수 감소
        replyRepository.delete(reply); //대댓글 삭제

    }


}
