package com.ssafy.passit.submission.controller;

import com.ssafy.passit.common.response.ApiResponse;
import com.ssafy.passit.submission.dto.CreateSubmissionRequest;
import com.ssafy.passit.submission.dto.SubmissionResultResponse;
import com.ssafy.passit.submission.service.SubmissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Submissions", description = "코드 제출 및 채점 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class SubmissionController {

    private final SubmissionService submissionService;

    @Operation(summary = "코드 제출 및 채점", description = "코딩 문제에 코드를 제출하고 채점합니다. language는 PYTHON / JAVA / CPP 중 하나입니다. 채점 결과: AC / WA / CE / RE / TLE / MLE")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "제출 및 채점 완료"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "문제 없음")
    })
    @PostMapping("/problems/{problemId}/submissions")
    public ApiResponse<SubmissionResultResponse> createSubmission(
        @Parameter(description = "코딩 문제 ID") @PathVariable Long problemId,
        @RequestBody CreateSubmissionRequest request
    ) {
        return ApiResponse.success(submissionService.submit(problemId, request));
    }

    @Operation(summary = "제출 결과 조회", description = "제출 ID로 채점 결과를 조회합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "제출 기록 없음")
    })
    @GetMapping("/submissions/{submissionId}")
    public ApiResponse<SubmissionResultResponse> getSubmission(
        @Parameter(description = "제출 ID") @PathVariable Long submissionId
    ) {
        return ApiResponse.success(submissionService.getSubmission(submissionId));
    }
}
