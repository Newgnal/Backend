package com.tave.alarmissue.report.dto.responsedto;

import com.tave.alarmissue.report.domain.Report;
import com.tave.alarmissue.report.domain.ReportType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ReportResponseDto {
    private Long reportId;
    private boolean reported;
    private ReportType reportType;
}
