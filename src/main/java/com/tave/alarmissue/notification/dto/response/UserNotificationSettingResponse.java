package com.tave.alarmissue.notification.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "사용자 알림 설정 응답")
public class UserNotificationSettingResponse {
    @Schema(description = "방해금지 모드 활성화 여부")
    private Boolean doNotDisturbEnabled;

    @Schema(description = "방해금지 시작 시간")
    private LocalTime doNotDisturbStartTime;

    @Schema(description = "방해금지 종료 시간")
    private LocalTime doNotDisturbEndTime;

    @Schema(description = "키워드 뉴스 알림 활성화")
    private Boolean keywordNewsEnabled;

    @Schema(description = "댓글 알림 활성화")
    private Boolean communityCommentEnabled;

    @Schema(description = "좋아요 알림 활성화")
    private Boolean communityLikeEnabled;

//    @Schema(description = "투표 마감 알림 활성화")
//    private Boolean communityVoteEndEnabled;

    @Schema(description = "답글 알림 활성화")
    private Boolean communityReplyEnabled;

    @Schema(description = "공지사항 알림 활성화")
    private Boolean announcementEnabled;
}