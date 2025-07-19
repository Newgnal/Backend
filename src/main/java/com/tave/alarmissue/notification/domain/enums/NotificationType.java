package com.tave.alarmissue.notification.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NotificationType {
    KEYWORD_NEWS("키워드 뉴스"),
    COMMUNITY_COMMENT("댓글"),
    COMMUNITY_LIKE("좋아요"),
    COMMUNITY_VOTE_END("투표 마감"),
    COMMUNITY_REPLY("답글"),
    ANNOUNCEMENT("공지사항");

    private final String displayName;
}