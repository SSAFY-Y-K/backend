package com.ssafy.passit.submission.controller;

import com.ssafy.passit.common.response.ApiResponse;
import com.ssafy.passit.security.UserPrincipal;
import com.ssafy.passit.submission.dto.CreateSubmissionRequest;
import com.ssafy.passit.submission.dto.SubmissionResultResponse;
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

@Tag(name = "Submissions", description = "肄붾뱶 ?쒖텧 諛?梨꾩젏 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class SubmissionController {

    private final SubmissionService submissionService;

    @Operation(summary = "肄붾뱶 ?쒖텧 諛?梨꾩젏", description = "肄붾뵫 臾몄젣??肄붾뱶瑜??쒖텧?섍퀬 梨꾩젏?⑸땲?? language??PYTHON / JAVA / CPP 以??섎굹?낅땲?? 梨꾩젏 寃곌낵: AC / WA / CE / RE / TLE / MLE")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "?쒖텧 諛?梨꾩젏 ?꾨즺"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "臾몄젣 ?놁쓬")
    })
    @PostMapping("/problems/{problemId}/submissions")
    public ApiResponse<SubmissionResultResponse> createSubmission(
        @AuthenticationPrincipal UserPrincipal principal,
        @Parameter(description = "肄붾뵫 臾몄젣 ID") @PathVariable Long problemId,
        @RequestBody CreateSubmissionRequest request
    ) {
        return ApiResponse.success(submissionService.submit(problemId, principal.getUserId(), request));
    }

    @Operation(summary = "?쒖텧 寃곌낵 議고쉶", description = "?쒖텧 ID濡?梨꾩젏 寃곌낵瑜?議고쉶?⑸땲??")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "議고쉶 ?깃났"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "?쒖텧 湲곕줉 ?놁쓬")
    })
    @GetMapping("/submissions/{submissionId}")
    public ApiResponse<SubmissionResultResponse> getSubmission(
        @AuthenticationPrincipal UserPrincipal principal,
        Authentication authentication,
        @Parameter(description = "?쒖텧 ID") @PathVariable Long submissionId
    ) {
        return ApiResponse.success(
            submissionService.getSubmission(submissionId, principal.getUserId(), isAdmin(authentication))
        );
    }

    private boolean isAdmin(Authentication authentication) {
        return authentication.getAuthorities().stream()
            .anyMatch(authority -> "ADMIN".equals(authority.getAuthority()));
    }
}
