package com.tave.alarmissue.post.dto.request;

import com.tave.alarmissue.post.domain.enums.ReportType;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReportCreateRequestDto {
    private boolean reported;
    private ReportType reportType;
}
