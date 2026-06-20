package com.ssafy.passit.report.service;

import com.ssafy.passit.common.exception.ApiException;
import com.ssafy.passit.common.exception.ErrorCode;
import com.ssafy.passit.problem.mapper.TestCaseMapper;
import com.ssafy.passit.problem.model.TestCase;
import com.ssafy.passit.problem.service.ProblemQueryService;
import com.ssafy.passit.report.dto.ReportRequest;
import com.ssafy.passit.report.dto.ReportResponse;
import com.ssafy.passit.report.dto.TestCaseDetailResponse;
import com.ssafy.passit.report.dto.TestCaseUpdateRequest;
import com.ssafy.passit.report.mapper.ProblemReportMapper;
import com.ssafy.passit.report.model.ProblemReport;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProblemReportService {

    private final ProblemReportMapper reportMapper;
    private final TestCaseMapper testCaseMapper;
    private final ProblemQueryService problemQueryService;

    public void report(Long problemId, ReportRequest request) {
        if (request.content() == null || request.content().isBlank()) {
            throw new ApiException(ErrorCode.INVALID_REQUEST, "신고 내용을 입력해주세요.");
        }
        problemQueryService.getCodingProblem(problemId);
        ProblemReport report = ProblemReport.builder()
            .problemId(problemId)
            .userId(request.userId())
            .content(request.content())
            .build();
        reportMapper.insert(report);
    }

    public List<ReportResponse> getAllReports() {
        return reportMapper.findAll().stream()
            .map(ReportResponse::from)
            .toList();
    }

    public List<ReportResponse> getReports(Long problemId) {
        return reportMapper.findAllByProblemId(problemId).stream()
            .map(ReportResponse::from)
            .toList();
    }

    public void resolve(Long reportId) {
        int updated = reportMapper.resolve(reportId);
        if (updated == 0) {
            throw new ApiException(ErrorCode.INVALID_REQUEST, "신고를 찾을 수 없습니다.");
        }
    }

    public List<TestCaseDetailResponse> getAllTestCases(Long problemId) {
        return testCaseMapper.findAllByProblemId(problemId).stream()
            .map(TestCaseDetailResponse::from)
            .toList();
    }

    public void updateTestCase(Long testCaseId, TestCaseUpdateRequest request) {
        if (request.inputData() == null || request.expectedOutput() == null) {
            throw new ApiException(ErrorCode.INVALID_REQUEST, "inputData와 expectedOutput은 필수입니다.");
        }
        int updated = testCaseMapper.update(testCaseId, request.inputData(), request.expectedOutput());
        if (updated == 0) {
            throw new ApiException(ErrorCode.INVALID_REQUEST, "테스트케이스를 찾을 수 없습니다.");
        }
    }
}
