package com.ssafy.passit.report.dto;

import com.ssafy.passit.report.model.ProblemReport;
import java.time.LocalDateTime;

public record ReportResponse(
    Long reportId,
    Long problemId,
    String problemTitle,
    String nickname,
    String content,
    String status,
    LocalDateTime createdAt
) {
    public static ReportResponse from(ProblemReport r) {
        return new ReportResponse(
            r.getReportId(),
            r.getProblemId(),
            r.getProblemTitle(),
            r.getNickname(),
            r.getContent(),
            r.getStatus(),
            r.getCreatedAt()
        );
    }
}
