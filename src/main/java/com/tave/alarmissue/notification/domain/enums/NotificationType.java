package com.tave.alarmissue.notification.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NotificationType {
    KEYWORD_NEWS("키워드 뉴스", true),
    COMMUNITY_COMMENT("댓글", false),
    COMMUNITY_LIKE("좋아요", false),
//    COMMUNITY_VOTE_END("투표 마감", false),
    COMMUNITY_REPLY("답글", false),
    ANNOUNCEMENT("공지사항", false);

    private final String displayName;
    private final boolean supportDoNotDisturb;  // 방해금지 시간 지원 여부
}