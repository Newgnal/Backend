package com.tave.alarmissue.news.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "대표 뉴스 정보")
public class RepresentativeNewsDto {
    private String title;
    private String source;
    private String timeAgo; //발행 시간으로 부터 얼마나 됬는지
}
