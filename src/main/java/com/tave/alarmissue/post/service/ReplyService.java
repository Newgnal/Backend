package com.tave.alarmissue.post.service;

import com.tave.alarmissue.post.domain.Comment;
import com.tave.alarmissue.post.repository.*;
import com.tave.alarmissue.post.domain.Post;
import com.tave.alarmissue.post.converter.ReplyConverter;
import com.tave.alarmissue.post.domain.Reply;
import com.tave.alarmissue.post.dto.request.ReplyCreateRequestDto;
import com.tave.alarmissue.post.dto.response.ReplyResponseDto;
import com.tave.alarmissue.post.exception.ReplyException;
import com.tave.alarmissue.user.domain.UserEntity;
import com.tave.alarmissue.user.repository.UserRepository;
import com.tave.alarmissue.post.domain.Vote;
import com.tave.alarmissue.post.domain.VoteType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Objects;
import static com.tave.alarmissue.post.exception.ReplyErrorCode.*;


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
    private final LikeRepository likeRepository ;

    @Transactional
    public ReplyResponseDto createReply(ReplyCreateRequestDto dto, Long commentId, Long userId) {
        UserEntity user = userRepository.findById(userId).
                orElseThrow(()->new ReplyException(USER_ID_NOT_FOUND,"사용자가 없습니다."));

        Comment comment = commentRepository.findById(commentId).
                orElseThrow(()->new ReplyException(COMMENT_ID_NOT_FOUND, "commentId: "+ commentId));

        Post post= postRepository.findById(comment.getPost().getPostId()).
                orElseThrow(()->new ReplyException(POST_ID_NOT_FOUND, "postId:" + comment.getPost().getPostId()));

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
    public void deleteReply(Long replyId, Long commentId, Long userId) {
        UserEntity user = userRepository.findById(userId).
                orElseThrow(()->new ReplyException(USER_ID_NOT_FOUND,"사용자가 없습니다."));

        Comment comment = commentRepository.findById(commentId).
                orElseThrow(()->new ReplyException(COMMENT_ID_NOT_FOUND, "commentId: "+ commentId));

        Post post= postRepository.findById(comment.getPost().getPostId()).
                orElseThrow(()->new ReplyException(POST_ID_NOT_FOUND, "postId:" + comment.getPost().getPostId()));

        Reply reply = replyRepository.findById(replyId).
                orElseThrow(()-> new ReplyException(REPLY_ID_NOT_FOUND, "replyId:" + replyId));

        if(!Objects.equals(comment.getCommentId(),reply.getComment().getCommentId())) {
            throw new ReplyException(COMMENT_ID_MIXMATCH," Reply의 commentId: "+reply.getComment().getCommentId() + " commentId: " + comment.getCommentId());
        }

        if(!Objects.equals(reply.getUser().getId(),user.getId())) {
            throw new ReplyException(REPLY_DELETE_FORBIDDEN,"Reply의 userId: "+reply.getUser().getId()+ " userId: " + user.getId());
        }
        likeRepository.deleteAllByReply(reply); //좋아요 삭제
        replyRepository.delete(reply); //대댓글 삭제
    }
}
