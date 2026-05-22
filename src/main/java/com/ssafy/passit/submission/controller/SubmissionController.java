package com.ssafy.passit.submission.controller;

import com.ssafy.passit.common.response.ApiResponse;
import com.ssafy.passit.submission.dto.CreateSubmissionRequest;
import com.ssafy.passit.submission.dto.SubmissionResultResponse;
import com.ssafy.passit.submission.service.SubmissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class SubmissionController {

    private final SubmissionService submissionService;

    @PostMapping("/problems/{problemId}/submissions")
    public ApiResponse<SubmissionResultResponse> createSubmission(
        @PathVariable Long problemId,
        @RequestBody CreateSubmissionRequest request
    ) {
        return ApiResponse.success(submissionService.submit(problemId, request));
    }

    @GetMapping("/submissions/{submissionId}")
    public ApiResponse<SubmissionResultResponse> getSubmission(@PathVariable Long submissionId) {
        return ApiResponse.success(submissionService.getSubmission(submissionId));
    }
}
