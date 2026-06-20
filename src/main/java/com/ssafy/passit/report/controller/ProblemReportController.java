package com.ssafy.passit.report.controller;

import com.ssafy.passit.common.response.ApiResponse;
import com.ssafy.passit.report.dto.ReportRequest;
import com.ssafy.passit.report.dto.ReportResponse;
import com.ssafy.passit.report.dto.TestCaseDetailResponse;
import com.ssafy.passit.report.dto.TestCaseUpdateRequest;
import com.ssafy.passit.report.service.ProblemReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Problem Reports", description = "문제 오류 신고 및 관리자 테스트케이스 수정 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ProblemReportController {

    private final ProblemReportService reportService;

    @Operation(summary = "오류 신고 제출")
    @PostMapping("/problems/{problemId}/reports")
    public ApiResponse<Void> report(
        @PathVariable Long problemId,
        @RequestBody ReportRequest request
    ) {
        reportService.report(problemId, request);
        return ApiResponse.success(null);
    }

    @Operation(summary = "[관리자] 전체 신고 목록 조회")
    @GetMapping("/admin/reports")
    public ApiResponse<List<ReportResponse>> getAllReports() {
        return ApiResponse.success(reportService.getAllReports());
    }

    @Operation(summary = "[관리자] 문제별 신고 목록 조회")
    @GetMapping("/admin/problems/{problemId}/reports")
    public ApiResponse<List<ReportResponse>> getReports(@PathVariable Long problemId) {
        return ApiResponse.success(reportService.getReports(problemId));
    }

    @Operation(summary = "[관리자] 신고 해결 처리")
    @PatchMapping("/admin/reports/{reportId}/resolve")
    public ApiResponse<Void> resolve(@PathVariable Long reportId) {
        reportService.resolve(reportId);
        return ApiResponse.success(null);
    }

    @Operation(summary = "[관리자] 전체 테스트케이스 조회")
    @GetMapping("/admin/problems/{problemId}/test-cases")
    public ApiResponse<List<TestCaseDetailResponse>> getAllTestCases(@PathVariable Long problemId) {
        return ApiResponse.success(reportService.getAllTestCases(problemId));
    }

    @Operation(summary = "[관리자] 테스트케이스 수정")
    @PutMapping("/admin/test-cases/{testCaseId}")
    public ApiResponse<Void> updateTestCase(
        @PathVariable Long testCaseId,
        @RequestBody TestCaseUpdateRequest request
    ) {
        reportService.updateTestCase(testCaseId, request);
        return ApiResponse.success(null);
    }
}
