package com.tave.alarmissue.report.dto.requestdto;

import com.tave.alarmissue.report.domain.ReportType;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReportCreateRequestDto {
    private boolean reported;
    private ReportType reportType;
}
