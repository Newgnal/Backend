package com.tave.alarmissue.news.service;

import com.tave.alarmissue.news.domain.NewsComment;
import com.tave.alarmissue.news.domain.NewsCommentLike;
import com.tave.alarmissue.news.dto.response.NewsCommentLikeResponse;
import com.tave.alarmissue.news.dto.response.NewsCommentLikeStatusResponse;
import com.tave.alarmissue.news.exceptions.NewsException;
import com.tave.alarmissue.news.repository.NewsCommentLikeRepository;
import com.tave.alarmissue.news.repository.NewsCommentRepository;
import com.tave.alarmissue.user.domain.UserEntity;
import com.tave.alarmissue.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;

import java.util.Optional;

import static com.tave.alarmissue.news.exceptions.NewsErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NewsCommentLikeService {

    private final NewsCommentLikeRepository newsCommentLikeRepository;
    private final NewsCommentRepository newsCommentRepository;
    private final UserRepository userRepository;
    
    //(좋아요/좋아요 취소)
    @Transactional
    public NewsCommentLikeResponse commentLike(Long commentId, Long userId){
        //댓글 존재 확인
        NewsComment comment = newsCommentRepository.findById(commentId).orElseThrow(() -> new NewsException(COMMENT_ID_NOT_FOUND, " 댓글을 찾을 수 없습니다."));
        
        //사용자 존재 확인
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new NewsException(USER_ID_NOT_FOUND, "사용자를 찾을 수 없습니다."));
        
        //기존 좋아요 확인
        Optional<NewsCommentLike> existingLike = newsCommentLikeRepository.findByCommentIdAndUserId(commentId, userId);
        
        boolean isLiked = false;
        if(existingLike.isPresent()){
            //이미 좋아요가 있으면 삭제(좋아요 취소)
            newsCommentLikeRepository.delete(existingLike.get());
            comment.decrementLikeCount();
            isLiked=false;
        }
        else{
            NewsCommentLike newLike=NewsCommentLike.builder()
                    .comment(comment)
                    .user(user)
                    .build();
            newsCommentLikeRepository.save(newLike);
            comment.incrementLikeCount();
            isLiked = true;
        }

        return new NewsCommentLikeResponse(commentId, isLiked, comment.getLikeCount());
    }


    @Transactional(readOnly = true)
    public NewsCommentLikeStatusResponse getLikeStatus(Long commentId, Long userId) {
        // 댓글 존재 확인
        NewsComment comment = newsCommentRepository.findById(commentId)
                .orElseThrow(() -> new NewsException(COMMENT_ID_NOT_FOUND, "댓글을 찾을 수 없습니다."));

        // 좋아요 상태 확인
        boolean isLiked = newsCommentLikeRepository.existsByCommentIdAndUserId(commentId, userId);

        return new NewsCommentLikeStatusResponse(
                commentId,
                userId,
                isLiked,
                comment.getLikeCount()
        );
    }


}
