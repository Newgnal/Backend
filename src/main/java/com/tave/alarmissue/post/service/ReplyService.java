package com.tave.alarmissue.post.service;

import com.tave.alarmissue.post.domain.PostComment;
import com.tave.alarmissue.post.repository.*;
import com.tave.alarmissue.post.domain.Post;
import com.tave.alarmissue.post.converter.PostReplyConverter;
import com.tave.alarmissue.post.domain.PostReply;
import com.tave.alarmissue.post.dto.request.ReplyCreateRequestDto;
import com.tave.alarmissue.post.dto.response.ReplyResponseDto;
import com.tave.alarmissue.post.exception.ReplyException;
import com.tave.alarmissue.user.domain.UserEntity;
import com.tave.alarmissue.user.repository.UserRepository;
import com.tave.alarmissue.post.domain.PostVote;
import com.tave.alarmissue.post.domain.enums.VoteType;
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
    private final PostReplyConverter replyConverter;
    private final VoteRepository voteRepository;
    private final LikeRepository likeRepository ;

    @Transactional
    public ReplyResponseDto createReply(ReplyCreateRequestDto dto, Long commentId, Long userId) {
        UserEntity user = userRepository.findById(userId).
                orElseThrow(()->new ReplyException(USER_ID_NOT_FOUND,"사용자가 없습니다."));

        PostComment postComment = commentRepository.findById(commentId).
                orElseThrow(()->new ReplyException(COMMENT_ID_NOT_FOUND, "commentId: "+ commentId));

        Post post= postRepository.findById(postComment.getPost().getPostId()).
                orElseThrow(()->new ReplyException(POST_ID_NOT_FOUND, "postId:" + postComment.getPost().getPostId()));

        PostVote vote = null;
        VoteType voteType = null;
        //게시글에 투표가 있고 실제 유저가 투표했을경우 voteType 넣기
        if (post.getHasVote()) {
            vote = voteRepository.findByUserAndPost(user, post).orElse(null);
            voteType = (vote != null) ? vote.getVoteType() : null;
        }

        PostReply reply = replyConverter.toReply(dto,user,post, postComment,voteType);
        PostReply saved = replyRepository.save(reply);

        return PostReplyConverter.toReplyResponseDto(saved);

    }

    @Transactional
    public void deleteReply(Long replyId, Long commentId, Long userId) {
        UserEntity user = userRepository.findById(userId).
                orElseThrow(()->new ReplyException(USER_ID_NOT_FOUND,"사용자가 없습니다."));

        PostComment postComment = commentRepository.findById(commentId).
                orElseThrow(()->new ReplyException(COMMENT_ID_NOT_FOUND, "commentId: "+ commentId));

        Post post= postRepository.findById(postComment.getPost().getPostId()).
                orElseThrow(()->new ReplyException(POST_ID_NOT_FOUND, "postId:" + postComment.getPost().getPostId()));

        PostReply reply = replyRepository.findById(replyId).
                orElseThrow(()-> new ReplyException(REPLY_ID_NOT_FOUND, "replyId:" + replyId));

        if(!Objects.equals(postComment.getCommentId(),reply.getPostComment().getCommentId())) {
            throw new ReplyException(COMMENT_ID_MIXMATCH," Reply의 commentId: "+reply.getPostComment().getCommentId() + " commentId: " + postComment.getCommentId());
        }

        if(!Objects.equals(reply.getUser().getId(),user.getId())) {
            throw new ReplyException(REPLY_DELETE_FORBIDDEN,"Reply의 userId: "+reply.getUser().getId()+ " userId: " + user.getId());
        }
        likeRepository.deleteAllByPostReply(reply); //좋아요 삭제
        replyRepository.delete(reply); //대댓글 삭제
    }
}
