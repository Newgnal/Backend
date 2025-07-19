package com.tave.alarmissue.notification.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "알림 목록 응답")
public class NotificationListResponse {
    @Schema(description = "알림 목록")
    private List<NotificationResponse> notifications;

    @Schema(description = "전체 개수")
    private Long totalCount;

    @Schema(description = "다음 페이지 존재 여부")
    private Boolean hasNext;

    @Schema(description = "읽지 않은 알림 개수")
    private Long unreadCount;
}