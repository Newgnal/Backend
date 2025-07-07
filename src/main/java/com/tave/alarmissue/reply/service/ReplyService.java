package com.tave.alarmissue.reply.service;

import com.tave.alarmissue.comment.domain.Comment;
import com.tave.alarmissue.comment.repository.CommentRepository;
import com.tave.alarmissue.post.domain.Post;
import com.tave.alarmissue.post.repository.PostRepository;
import com.tave.alarmissue.reply.converter.ReplyConverter;
import com.tave.alarmissue.reply.domain.Reply;
import com.tave.alarmissue.reply.dto.request.ReplyCreateRequestDto;
import com.tave.alarmissue.reply.dto.response.ReplyResponseDto;
import com.tave.alarmissue.reply.exception.ReplyException;
import com.tave.alarmissue.reply.repository.ReplyRepository;
import com.tave.alarmissue.user.domain.UserEntity;
import com.tave.alarmissue.user.repository.UserRepository;
import com.tave.alarmissue.vote.domain.Vote;
import com.tave.alarmissue.vote.domain.VoteType;
import com.tave.alarmissue.vote.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Objects;
import static com.tave.alarmissue.reply.exception.ReplyErrorCode.*;


@RequiredArgsConstructor
@Service
@Slf4j
@Transactional(readOnly = true)
public class ReplyService {
    private final ReplyRepository replyRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final ReplyConverter replyConverter;
    private final VoteRepository voteRepository;


    @Transactional
    public ReplyResponseDto createReply(ReplyCreateRequestDto dto, Long postId, Long commentId, Long userId) {
        UserEntity user = userRepository.findById(userId).
                orElseThrow(()->new ReplyException(USER_ID_NOT_FOUND,"사용자가 없습니다."));

        Post post= postRepository.findById(postId).
                orElseThrow(()->new ReplyException(POST_ID_NOT_FOUND, "postId:" + postId));

        Comment comment = commentRepository.findById(commentId).
                orElseThrow(()->new ReplyException(COMMENT_ID_NOT_FOUND, "commentId: "+ commentId));

        if(!Objects.equals(post.getPostId(),comment.getPost().getPostId())) {
            throw new ReplyException(POST_ID_MIXMATCH, "comment의 postId: "+comment.getPost().getPostId() + " postId: " + post.getPostId());
        }

        Vote vote = null;
        VoteType voteType = null;
        //게시글에 투표가 있고 실제 유저가 투표했을경우 voteType 넣기
        if (post.getHasVote()) {
            vote = voteRepository.findByUserAndPost(user, post).orElse(null);
            voteType = (vote != null) ? vote.getVoteType() : null;
        }

        Reply reply = replyConverter.toReply(dto,user,post,comment,voteType);
        Reply saved = replyRepository.save(reply);

        return ReplyConverter.toReplyResponseDto(saved);

    }

    @Transactional
    public void deleteReply(Long replyId,Long postId, Long commentId, Long userId) {
        UserEntity user = userRepository.findById(userId).
                orElseThrow(()->new ReplyException(USER_ID_NOT_FOUND,"사용자가 없습니다."));

        Post post= postRepository.findById(postId).
                orElseThrow(()->new ReplyException(POST_ID_NOT_FOUND, "postId:" + postId));

        Comment comment = commentRepository.findById(commentId).
                orElseThrow(()->new ReplyException(COMMENT_ID_NOT_FOUND, "commentId: "+ commentId));

        Reply reply = replyRepository.findById(replyId).
                orElseThrow(()-> new ReplyException(REPLY_ID_NOT_FOUND, "replyId:" + replyId));

        if(!Objects.equals(post.getPostId(),comment.getPost().getPostId())) {
            throw new ReplyException(POST_ID_MIXMATCH, "comment의 postId: "+comment.getPost().getPostId() + " postId: " + post.getPostId());
        }

        if(!Objects.equals(comment.getCommentId(),reply.getComment().getCommentId())) {
            throw new ReplyException(COMMENT_ID_MIXMATCH," Reply의 commentId: "+reply.getComment().getCommentId() + " commentId: " + comment.getCommentId());
        }

        if(!Objects.equals(reply.getUser().getId(),user.getId())) {
            throw new ReplyException(REPLY_DELETE_FORBIDDEN,"Reply의 userId: "+reply.getUser().getId()+ " userId: " + user.getId());
        }
        replyRepository.delete(reply);
    }
}
