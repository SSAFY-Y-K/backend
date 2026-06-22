package com.ssafy.passit.report.controller;

import com.ssafy.passit.common.response.ApiResponse;
import com.ssafy.passit.report.dto.ReportRequest;
import com.ssafy.passit.report.dto.ReportResponse;
import com.ssafy.passit.report.dto.TestCaseDetailResponse;
import com.ssafy.passit.report.dto.TestCaseUpdateRequest;
import com.ssafy.passit.report.service.ProblemReportService;
import com.ssafy.passit.security.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Problem Reports", description = "臾몄젣 ?ㅻ쪟 ?좉퀬 諛?愿由ъ옄 ?뚯뒪?몄??댁뒪 ?섏젙 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ProblemReportController {

    private final ProblemReportService reportService;

    @Operation(summary = "?ㅻ쪟 ?좉퀬 ?쒖텧")
    @PostMapping("/problems/{problemId}/reports")
    public ApiResponse<Void> report(
        @AuthenticationPrincipal UserPrincipal principal,
        @PathVariable Long problemId,
        @RequestBody ReportRequest request
    ) {
        reportService.report(problemId, principal.getUserId(), request);
        return ApiResponse.success(null);
    }

    @Operation(summary = "[愿由ъ옄] ?꾩껜 ?좉퀬 紐⑸줉 議고쉶")
    @GetMapping("/admin/reports")
    public ApiResponse<List<ReportResponse>> getAllReports() {
        return ApiResponse.success(reportService.getAllReports());
    }

    @Operation(summary = "[愿由ъ옄] 臾몄젣蹂??좉퀬 紐⑸줉 議고쉶")
    @GetMapping("/admin/problems/{problemId}/reports")
    public ApiResponse<List<ReportResponse>> getReports(@PathVariable Long problemId) {
        return ApiResponse.success(reportService.getReports(problemId));
    }

    @Operation(summary = "[愿由ъ옄] ?좉퀬 ?닿껐 泥섎━")
    @PatchMapping("/admin/reports/{reportId}/resolve")
    public ApiResponse<Void> resolve(@PathVariable Long reportId) {
        reportService.resolve(reportId);
        return ApiResponse.success(null);
    }

    @Operation(summary = "[愿由ъ옄] ?꾩껜 ?뚯뒪?몄??댁뒪 議고쉶")
    @GetMapping("/admin/problems/{problemId}/test-cases")
    public ApiResponse<List<TestCaseDetailResponse>> getAllTestCases(@PathVariable Long problemId) {
        return ApiResponse.success(reportService.getAllTestCases(problemId));
    }

    @Operation(summary = "[愿由ъ옄] ?뚯뒪?몄??댁뒪 ?섏젙")
    @PutMapping("/admin/test-cases/{testCaseId}")
    public ApiResponse<Void> updateTestCase(
        @PathVariable Long testCaseId,
        @RequestBody TestCaseUpdateRequest request
    ) {
        reportService.updateTestCase(testCaseId, request);
        return ApiResponse.success(null);
    }
}
