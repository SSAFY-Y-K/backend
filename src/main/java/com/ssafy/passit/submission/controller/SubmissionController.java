package com.ssafy.passit.submission.controller;

import com.ssafy.passit.common.response.ApiResponse;
import com.ssafy.passit.security.UserPrincipal;
import com.ssafy.passit.submission.dto.CreateSubmissionRequest;
import com.ssafy.passit.submission.dto.SubmissionResultResponse;
import com.ssafy.passit.submission.dto.SubmissionSourceResponse;
import com.ssafy.passit.submission.service.SubmissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Submissions", description = "Code submission APIs")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class SubmissionController {

    private final SubmissionService submissionService;

    @Operation(
        summary = "Create a submission",
        description = "Submits source code for a coding problem and returns the final judge result."
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Submission completed"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Problem not found")
    })
    @PostMapping("/problems/{problemId}/submissions")
    public ApiResponse<SubmissionResultResponse> createSubmission(
        @AuthenticationPrincipal UserPrincipal principal,
        @Parameter(description = "Problem ID") @PathVariable Long problemId,
        @RequestBody CreateSubmissionRequest request
    ) {
        return ApiResponse.success(submissionService.submit(problemId, principal.getUserId(), request));
    }

    @Operation(
        summary = "Get submission result",
        description = "Returns the judge result for a submission."
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Submission result returned"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Submission not found")
    })
    @GetMapping("/submissions/{submissionId}")
    public ApiResponse<SubmissionResultResponse> getSubmission(
        @AuthenticationPrincipal UserPrincipal principal,
        Authentication authentication,
        @Parameter(description = "Submission ID") @PathVariable Long submissionId
    ) {
        return ApiResponse.success(
            submissionService.getSubmission(submissionId, principal.getUserId(), isAdmin(authentication))
        );
    }

    @Operation(
        summary = "Get submission source",
        description = "Returns the submitted source code for the owner or an admin."
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Submission source returned"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Only the owner or an admin can view the source"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Submission not found")
    })
    @GetMapping("/submissions/{submissionId}/source")
    public ApiResponse<SubmissionSourceResponse> getSubmissionSource(
        @AuthenticationPrincipal UserPrincipal principal,
        Authentication authentication,
        @Parameter(description = "Submission ID") @PathVariable Long submissionId
    ) {
        return ApiResponse.success(
            submissionService.getSubmissionSource(submissionId, principal.getUserId(), isAdmin(authentication))
        );
    }

    private boolean isAdmin(Authentication authentication) {
        return authentication.getAuthorities().stream()
            .anyMatch(authority -> "ADMIN".equals(authority.getAuthority()));
    }
}
